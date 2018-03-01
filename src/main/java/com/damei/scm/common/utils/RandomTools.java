package com.damei.scm.common.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * @author zhangmin 随机数帮助类
 **/
public final class RandomTools {

    private static final String RAND_CHARS = "0123456789abcdefghigklmnopqrstuvtxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";

    private static final int[] prefix = {1, 2, 3, 4, 5, 6, 7, 8, 9};

    private static Random random = new Random();

    /***
     * 产生[0-max)范围的随机数
     **/
    public static int rand(int max) {
        return random.nextInt(max);
    }

    /***
     * 产生[min-max)范围的随机数 如果@param min>=max 直接返回 min 如果 min<max && min<0 则返回[0-max)范围的随机数
     */
    public static int rand(int min, int max) {
        if (min < max) {
            if (min > 0) {
                return rand(max - min) + min;
            } else {
                return rand(max);
            }
        } else {
            return min;
        }
    }

    /**
     * 返回长度为length的随机字符串
     *
     * @param length                                      要生成随机字符串的长度
     * @param randType==0,则包括所有的字符的字符串,randType==1，返回字母串, randType==2 返回数字串
     */
    public static String getRandStr(int length, int randType) {
        StringBuffer hash = new StringBuffer(length);
        if (randType == 1) {//返回字母串
            for (int i = 0; i < length; i++)
                hash.append(RAND_CHARS.charAt(10 + random.nextInt(52)));
        } else if (randType == 2) {//返回数字串
            for (int i = 0; i < length; i++)
                hash.append(RAND_CHARS.charAt(random.nextInt(10)));
        } else {
            for (int i = 0; i < length; i++)
                hash.append(RAND_CHARS.charAt(random.nextInt(62)));
        }
        return hash.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     *
     * @param num       数字
     * @param fixdlenth 字符串长度
     * @return 定长的字符串
     */
    public static String toFixdLengthString(int num, int fixdlenth) {
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if (fixdlenth - strNum.length() >= 0) {
            sb.append(generateZeroString(fixdlenth - strNum.length()));
        } else {
            throw new RuntimeException("将数字" + num + "转化为长度为" + fixdlenth + "的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 随机产生最大为18位的long型数据(long型数据的最大值是9223372036854775807,共有19位)
     *
     * @param digit 用户指定随机数据的位数
     */
    public static long randomLong(int digit) {
        if (digit >= 19 || digit <= 0)
            throw new IllegalArgumentException("digit should between 1 and 18(1<=digit<=18)");
        String s = RandomStringUtils.randomNumeric(digit - 1);
        return Long.parseLong(getPrefix() + s);
    }

    /**
     * 随机产生在指定位数之间的long型数据,位数包括两边的值,minDigit<=maxDigit
     *
     * @param minDigit 用户指定随机数据的最小位数 minDigit>=1
     * @param maxDigit 用户指定随机数据的最大位数 maxDigit<=18
     */
    public static long randomLong(int minDigit, int maxDigit) {
        if (minDigit > maxDigit) {
            throw new IllegalArgumentException("minDigit > maxDigit");
        }
        if (minDigit <= 0 || maxDigit >= 19) {
            throw new IllegalArgumentException("minDigit <=0 || maxDigit>=19");
        }
        return randomLong(minDigit + getDigit(maxDigit - minDigit));
    }

    private static int getDigit(int max) {
        return random.nextInt(max + 1);
    }

    /**
     * 保证第一位不是零
     *
     * @return
     */
    private static String getPrefix() {
        return prefix[random.nextInt(prefix.length)] + "";
    }

    /**
     * 生成一个定长的纯0字符串
     *
     * @param length 字符串长度
     * @return 纯0字符串
     */
    private static String generateZeroString(int length) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append('0');
        }
        return sb.toString();
    }
}