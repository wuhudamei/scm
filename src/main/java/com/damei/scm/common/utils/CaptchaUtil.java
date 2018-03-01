package com.damei.scm.common.utils;


import org.patchca.color.SingleColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.AdaptiveRandomWordFactory;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 图片验证码工具类.
 */
public class CaptchaUtil {

    private static ConfigurableCaptchaService cs;

    private static ConfigurableCaptchaService getDefaultCS() {
        if (cs == null) {
            cs = new ConfigurableCaptchaService();
            cs.setWordFactory(new WordFactory(4));
            cs.setColorFactory(new SingleColorFactory(new Color(25, 60, 170)));
            cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
        }
        return cs;
    }

    /**
     * 输出图片验证码，返回验证码字符串.
     *
     * @param out    输出图片验证码
     * @param format 图片格式，如：png, jpg
     * @return 验证码字符串
     */
    public static String captcha(OutputStream out, String format) {
        try {
            return EncoderHelper.getChallangeAndWriteImage(getDefaultCS(), format, out);
        } catch (IOException e) {
            throw new RuntimeException("输出图片验证码出错", e);
        }
    }

    /**
     * 此类继承默认的 AdaptiveRandomWordFactory 用来设定密码长度.
     */
    static class WordFactory extends AdaptiveRandomWordFactory {
        public WordFactory() {
        }

        public WordFactory(int length) {
            this.minLength = length;
            this.maxLength = length;
        }
    }
}
