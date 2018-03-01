package com.damei.scm.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

/**
 * 拼音工具类
 */
public final class PingYinUtil {

	static HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
	static HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();

	static {
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);

		defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
	}

	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param chineseString
	 */
	public static String getPingYin(String chineseString) {
		if (StringUtils.isBlank(chineseString)) {
			return StringUtils.EMPTY;
		}

		char[] input = chineseString.trim().toCharArray();
		StringBuilder buf = new StringBuilder();
		try {
			for (int i = 0; i < input.length; i++) {
				if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
					buf.append(temp[0]);
				} else {
					buf.append(Character.toString(input[i]));
				}
			}
		} catch (BadHanyuPinyinOutputFormatCombination e) {
			e.printStackTrace();
		}
		return buf.toString();
	}

	/**
	 * 获取汉字串拼音首字母，英文字符不变
	 * 
	 * @param chinese 汉字串
	 * @return 汉语拼音首字母
	 */
	public static String getFirstSpell(String chinese) {
		if (StringUtils.isBlank(chinese)) {
			return StringUtils.EMPTY;
		}

		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
					if (temp != null) {
						pybf.append(temp[0].charAt(0));
					}
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString().replaceAll("\\W", "").trim();
	}

	/**
	 * 获取汉字串拼音，英文字符不变
	 * 
	 * @param chinese 汉字串
	 * @return 汉语拼音
	 */
	public static String getFullSpell(String chinese) {
		if (StringUtils.isBlank(chinese)) {
			return StringUtils.EMPTY;
		}

		StringBuffer pybf = new StringBuffer();
		char[] arr = chinese.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] > 128) {
				try {
					pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pybf.append(arr[i]);
			}
		}
		return pybf.toString();
	}

	public static void main(String[] args) {
		String chinese = "aa1淡水泉投资";
		String pinY = getPingYin(chinese);
		String fPiny = getFirstSpell(chinese);
		System.out.println(pinY);
		System.out.println(fPiny);
	}
}