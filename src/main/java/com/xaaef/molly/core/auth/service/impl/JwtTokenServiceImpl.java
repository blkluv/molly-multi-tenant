package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.xaaef.molly.core.auth.exception.JwtAuthException;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.redis.RedisCacheUtils;
import com.xaaef.molly.core.tenant.util.TenantUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xaaef.molly.common.util.JsonUtils.DEFAULT_DATE_TIME_PATTERN;
import static com.xaaef.molly.core.auth.consts.LoginConst.*;
import static com.xaaef.molly.core.auth.enums.OAuth2Error.TOKEN_FORMAT_ERROR;


/**
 * <p>
 * 服务端 jwt token 认证
 * </p>
 *
 * @author WangChenChen
 * @version 1.0
 * @date 2022/3/22 15:24
 */

@Slf4j
public class JwtTokenServiceImpl implements JwtTokenService {

    private final JwtTokenProperties props;

    private final RedisCacheUtils cacheUtils;

    private final JWTSigner signer;

    public JwtTokenServiceImpl(JwtTokenProperties props, RedisCacheUtils cacheUtils) {
        this.props = props;
        this.cacheUtils = cacheUtils;
        this.signer = JWTSignerUtil.hs256(props.getSecret().getBytes());
    }


    private void removeLoginUser(String loginId) {
        cacheUtils.deleteKey(loginId);
    }


    @Override
    public JwtLoginUser getLoginUser(String loginId) {
        return cacheUtils.getObject(LOGIN_TOKEN_KEY + loginId, JwtLoginUser.class);
    }


    @Override
    public void setLoginUser(JwtLoginUser loginUser) {
        String loginKey = LOGIN_TOKEN_KEY + loginUser.getLoginId();

        // 将随机id 跟 当前登录的用户关联，在一起！
        cacheUtils.setObject(loginKey, loginUser, Duration.ofSeconds(props.getTokenExpired()));

        // 判断是否开启 单点登录
        if (props.getSso()) {
            // 拼接，当前在线用户 online_user:master:admin
            String onlineUserKey = StrUtil.format("{}{}:{}", ONLINE_USER_KEY, loginUser.getTenantId(), loginUser.getUsername());
            String oldLoginKey = cacheUtils.getString(onlineUserKey);
            // 判断用户名。是否已经登录了！
            if (StringUtils.isNotBlank(oldLoginKey)) {
                // 移除之前登录的用户
                removeLoginUser(LOGIN_TOKEN_KEY + oldLoginKey);

                // 移除在线用户
                removeLoginUser(onlineUserKey);

                // 获取当前时间
                String milli = LocalDateTimeUtil.format(LocalDateTime.now(), DEFAULT_DATE_TIME_PATTERN);

                // 将 被强制挤下线的用户，以及时间，保存到 redis中，提示给前端用户！
                cacheUtils.setString(
                        FORCED_OFFLINE_KEY + oldLoginKey,
                        milli, Duration.ofSeconds(props.getPromptExpired())
                );
            }

            // 保存 在线用户
            cacheUtils.setString(onlineUserKey, loginUser.getLoginId(), Duration.ofSeconds(props.getTokenExpired()));
        }
    }


    @Override
    public JwtLoginUser validate(String bearerToken) throws JwtAuthException {
        var tokenStr = bearerToken.substring(props.getTokenType().length());
        JWT jwt = null;
        try {
            jwt = JWTUtil.parseToken(tokenStr);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new JwtAuthException(TOKEN_FORMAT_ERROR);
        }
        // 获取到 用户的唯一登录ID
        var loginId = jwt.getPayloads().getStr(JWT.SUBJECT);

        // 根据登录 唯一登录ID 从redis中获取登录的用户信息
        JwtLoginUser jwtUser = getLoginUser(loginId);

        // 如果此用户为空。判断是否开启了单点登录
        if (jwtUser == null || StringUtils.isEmpty(jwtUser.getUsername())) {
            // 判断是否启用单点登录
            if (props.getSso()) {
                String key = FORCED_OFFLINE_KEY + loginId;
                // 判断此用户，是不是被挤下线
                String offlineTime = cacheUtils.getString(key);
                if (StringUtils.isNotBlank(offlineTime)) {
                    // 删除 被挤下线 的消息提示
                    removeLoginUser(key);
                    String errMsg = String.format("您的账号在[ %s ]被其他用户拥下线了！", offlineTime);
                    log.info("errMsg {}", errMsg);
                    throw new JwtAuthException(errMsg);
                }
            }
            throw new JwtAuthException("当前登录用户不存在");
        }

        return jwtUser;
    }


    @Override
    public JwtTokenValue refresh() {
        var oldLoginUser = JwtSecurityUtils.getLoginUser();

        String loginKey = LOGIN_TOKEN_KEY + oldLoginUser.getLoginId();

        // 移除登录的用户。根据tokenId
        removeLoginUser(loginKey);

        // 生成一个随机ID 跟当前用户关联
        String loginId = IdUtil.simpleUUID();
        var token = createJwtStr(loginId);
        oldLoginUser.setLoginId(loginId);

        setLoginUser(oldLoginUser);

        return JwtTokenValue.builder()
                .header(props.getTokenHeader())
                .accessToken(token)
                .tokenType(props.getTokenType())
                .expiresIn(props.getTokenExpired())
                .build();
    }


    @Override
    public void logout() {
        var loginUser = JwtSecurityUtils.getLoginUser();
        String loginKey = LOGIN_TOKEN_KEY + loginUser.getLoginId();
        // 移除登录的用户。根据tokenId
        removeLoginUser(loginKey);
    }


    @Override
    public String createJwtStr(String id) {
        return JWT.create().setSigner(signer).setSubject(id).sign();
    }


    @Override
    public Set<String> listUsernames() {
        String onlineUserKey = StrUtil.format("{}{}:{}", ONLINE_USER_KEY, TenantUtils.getTenantId(), "*");
        String onlineUserKeyPrefix = StrUtil.format("{}{}:", ONLINE_USER_KEY, TenantUtils.getTenantId());
        return Objects.requireNonNull(cacheUtils.keys(onlineUserKey))
                .stream()
                .map(r -> r.replaceAll(onlineUserKeyPrefix, ""))
                .collect(Collectors.toSet());
    }


    @Override
    public Set<String> listLoginIds() {
        String onlineLoginKey = StrUtil.format("{}*", LOGIN_TOKEN_KEY);
        return Objects.requireNonNull(cacheUtils.keys(onlineLoginKey))
                .stream()
                .map(r -> r.replaceAll(LOGIN_TOKEN_KEY, ""))
                .collect(Collectors.toSet());
    }


    @Override
    public Set<JwtLoginUser> listLoginUsers() {
        return this.listLoginIds()
                .stream()
                .map(this::getLoginUser)
                .collect(Collectors.toSet());
    }


    @Override
    public JwtTokenProperties getProps() {
        return props;
    }

}
