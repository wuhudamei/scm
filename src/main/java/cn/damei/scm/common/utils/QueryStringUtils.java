package cn.damei.scm.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liuwei
 */
public class QueryStringUtils {

    /**
     * 传入queryString，返回去除pageNumber参数之后的部分。 例如：
     * <ul>
     * <li>"pageNumber=5&a=1&b=2" 结果 "a=1&b=2"</li>
     * <li>"a=1&b=2&pageNumber=5" 结果 "a=1&b=2"</li>
     * <li>null 结果 ""</li>
     * </ul>
     *
     * @param queryString 未解码的查询字符串
     * @return 去除pageNumber参数之后的部分
     */
    public static String removePageNumber(String queryString) {
        if (queryString != null) {
            return queryString.replaceAll("(^|&)pageNumber=(\\d*?)(&|$)", "$1").replaceAll("&$", "");
        }
        return "";
    }

    /**
     * 从sourceStr中 筛选出 匹配正在表达式regex的 内容
     **/
    public static List<String> findMatchsContentsByRegex(String sourceStr, String regex) {
        List<String> strList = new ArrayList<String>(0);
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(sourceStr);
            while (matcher.find()) {
                strList.add(matcher.group());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strList;
    }
}
