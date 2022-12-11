package com.xaaef.molly.core.auth.service.impl;

import cn.hutool.captcha.CaptchaUtil;
import com.xaaef.molly.core.auth.jwt.JwtTokenProperties;
import com.xaaef.molly.core.auth.service.LineCaptchaService;
import com.xaaef.molly.core.redis.RedisCacheUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.time.Duration;

import static com.xaaef.molly.core.auth.consts.LoginConst.CAPTCHA_CODE_KEY;


/**
 * <p>
 *
 * </p>
 *
 * @author Wang Chen Chen<932560435@qq.com>
 * @version 1.0
 * @createTime 2020/3/5 0005 11:32
 */

@Slf4j
@Service
@AllArgsConstructor
public class LineCaptchaServiceImpl implements LineCaptchaService {

    private final RedisCacheUtils cacheUtils;

    private final JwtTokenProperties props;

    @Override
    public BufferedImage random(String codeKey) {
        var lineCaptcha = CaptchaUtil.createLineCaptcha(120, 50);
        // 将验证码的 codeKey 和 codeText , 保存在 redis 中，有效时间为 10 分钟
        cacheUtils.setString(
                CAPTCHA_CODE_KEY + codeKey,
                lineCaptcha.getCode(),
                Duration.ofSeconds(props.getCaptchaExpired())
        );
        return lineCaptcha.getImage();
    }


    @Override
    public void delete(String codeKey) {
        cacheUtils.deleteKey(CAPTCHA_CODE_KEY + codeKey);
    }


    @Override
    public boolean check(String codeKey, String userCodeText) {
        // 获取服务器的 CodeText
        String serverCodeText = cacheUtils.getString(CAPTCHA_CODE_KEY + codeKey);
        // 将 serverCodeText 和 user.codeText 都转换成小写，然后比较
        return StringUtils.equalsIgnoreCase(serverCodeText, userCodeText);
    }

}
