package com.mdni.scm.web.filter;

import com.google.common.collect.Maps;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author zhangmin
 *         清除Sql关键词的包装器
 */
public class StopWordHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String[] stopWords;

    public StopWordHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public StopWordHttpServletRequestWrapper(HttpServletRequest request, String stopwords) {
        super(request);
        this.stopWords = StringUtils.split(stopwords, "|");
    }


    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        return cleanStopWords(value);
    }


    public String[] getParameterValues(String name) {
        String[] paramValues = super.getParameterValues(name);
        if (paramValues == null)
            return null;
        int len = paramValues.length;
        String[] values = new String[len];
        for (int j = 0; j < len; j++)
            values[j] = cleanStopWords(paramValues[j]);
        return values;
    }


    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> paramMap = super.getParameterMap();
        if (MapUtils.isNotEmpty(paramMap)) {
            Map<String, String[]> escapedMap = Maps.newLinkedHashMap();

            Iterator<String> paramNameIter = paramMap.keySet().iterator();
            while (paramNameIter.hasNext()) {
                final String paramName = paramNameIter.next();
                escapedMap.put(paramName, getParameterValues(paramName));
            }
            return escapedMap;
        }
        return paramMap;
    }


    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return cleanStopWords(value);
    }


    private String cleanStopWords(String paramValue) {
        if (ArrayUtils.isNotEmpty(stopWords) && StringUtils.isNotBlank(paramValue)) {
            final String wordBoundary = "\\b";
            for (String stop : stopWords) {
                String stopPattern = wordBoundary + stop + wordBoundary;
                paramValue = Pattern.compile(stopPattern, Pattern.CASE_INSENSITIVE).matcher(paramValue).replaceAll(StringUtils.EMPTY);
            }
        }
        return paramValue;
    }

}