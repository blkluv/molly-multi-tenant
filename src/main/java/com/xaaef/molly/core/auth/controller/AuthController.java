package com.xaaef.molly.core.auth.controller;

import cn.hutool.core.util.StrUtil;
import com.xaaef.molly.common.util.JsonResult;
import com.xaaef.molly.common.util.JsonUtils;
import com.xaaef.molly.common.util.ServletUtils;
import com.xaaef.molly.core.auth.jwt.JwtLoginUser;
import com.xaaef.molly.core.auth.jwt.JwtSecurityUtils;
import com.xaaef.molly.core.auth.jwt.JwtTokenValue;
import com.xaaef.molly.core.auth.po.LoginFormPO;
import com.xaaef.molly.core.auth.service.JwtTokenService;
import com.xaaef.molly.core.auth.service.LineCaptchaService;
import com.xaaef.molly.core.auth.service.UserLoginService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.authentication.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

import static com.xaaef.molly.core.auth.consts.JwtConst.*;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


/**
 * <p>
 * 用户认证 控制器
 * </p>
 *
 * @author Wang Chen Chen <932560435@qq.com>
 * @version 3.0
 */


@Slf4j
@RestController
@AllArgsConstructor
public class AuthController {

    private final JwtTokenService jwtTokenService;

    private final UserLoginService loginService;

    private final LineCaptchaService captchaService;

    /**
     * TODO [json] 用户登录
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @PostMapping(value = LOGIN_URL, consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public JsonResult<JwtTokenValue> login(@RequestBody @Validated LoginFormPO user,
                                           HttpServletRequest request, BindingResult br) {
        if (br.hasErrors()) {
            String err = Objects.requireNonNull(br.getFieldError()).getDefaultMessage();
            return JsonResult.fail(err, JwtTokenValue.class);
        }
        return login(user, request);
    }


    /**
     * TODO [Form Data] 用户登录
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @PostMapping(value = LOGIN_URL, consumes = APPLICATION_FORM_URLENCODED_VALUE, produces = APPLICATION_FORM_URLENCODED_VALUE)
    public JsonResult<JwtTokenValue> login2(@Validated LoginFormPO user,
                                            HttpServletRequest request, BindingResult br) {
        if (br.hasErrors()) {
            String err = Objects.requireNonNull(br.getFieldError()).getDefaultMessage();
            return JsonResult.fail(err, JwtTokenValue.class);
        }
        return login(user, request);
    }


    public JsonResult<JwtTokenValue> login(LoginFormPO user, HttpServletRequest request) {
        try {
            var tokenValue = loginService.login(user, request);
            return JsonResult.success("登录成功", tokenValue);
        } catch (Exception failed) {
            String msg = null;
            if (failed instanceof UsernameNotFoundException) {
                msg = StrUtil.format("用户名 {} 不存在", user.getUsername());
            } else if (failed instanceof BadCredentialsException) {
                msg = StrUtil.format("用户名 {} 密码输入错误", user.getUsername());
            } else if (failed instanceof DisabledException) {
                msg = StrUtil.format("用户名 {} 已被禁用", user.getUsername());
            } else if (failed instanceof LockedException) {
                msg = "抱歉您的账户已被锁定";
            } else if (failed instanceof AccountExpiredException) {
                msg = StrUtil.format("用户名 {} 已过期", user.getUsername());
            } else {
                msg = StrUtil.format(failed.getMessage());
            }
            return JsonResult.fail(msg, JwtTokenValue.class);
        }
    }


    /**
     * TODO 退出登录
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @PostMapping(LOGOUT_URL)
    public JsonResult<String> logout() {
        jwtTokenService.logout();
        return JsonResult.success();
    }


    /**
     * TODO 获取登录的用户信息
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @GetMapping(LOGIN_USER_URL)
    public JsonResult<JwtLoginUser> loginUser() {
        var auth = JwtSecurityUtils.getLoginUser();
        log.info(JsonUtils.toFormatJson(jwtTokenService.getOnlineUsers()));
        return JsonResult.success(auth);
    }


    /**
     * TODO 刷新 token
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @GetMapping(REFRESH_URL)
    public JsonResult<JwtTokenValue> refresh() {
        var refresh = jwtTokenService.refresh();
        return JsonResult.success(refresh);
    }


    /**
     * TODO 获取验证码
     *
     * @author WangChenChen
     * @date 2022/12/11 8:58
     */
    @GetMapping(CAPTCHA_CODES_URL)
    public void imageVerifyCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String codeKey = request.getParameter("codeKey");
        if (StringUtils.isBlank(codeKey) || StringUtils.length(codeKey) < 16) {
            ServletUtils.renderError(response, JsonResult.fail("参数codeKey 必须填写！并且长度要大于16位字符"));
            return;
        }
        // 设置响应的类型格式为图片格式
        response.setContentType("image/png");
        // 禁止图像缓存。
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        var image = captchaService.random(codeKey);
        // 获取 图片 验证码
        ImageIO.write(
                image,
                "PNG",
                response.getOutputStream()
        );
    }


}
