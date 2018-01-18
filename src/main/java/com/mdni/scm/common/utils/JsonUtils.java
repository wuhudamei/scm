package com.mdni.scm.common.utils;

import com.fasterxml.jackson.databind.JavaType;
import com.mdni.scm.common.Constants;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springside.modules.mapper.JsonMapper;
import org.springside.modules.web.Servlets;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unchecked")
public final class JsonUtils {

    public static final String DEFAULT_ENCODING = "UTF-8";

    //-- Content Type 定义 --//
    public static final String EXCEL_TYPE = "application/vnd.ms-excel";
    public static final String HTML_TYPE = "text/html";
    public static final String JS_TYPE = "text/javascript";
    public static final String JSON_TYPE = "application/json";
    public static final String XML_TYPE = "text/xml";
    public static final String TEXT_TYPE = "text/plain";
    public static final String MESSAGE_ATTRIBUTE_NAME = "actionMessage";
    public static final String CALL_BACK = "callback";
    // 打印全部属性
    public final static JsonMapper normalMapper = new JsonMapper();
    //-- header 常量定义 --//
    private static final String HEADER_ENCODING = "encoding";
    private static final String HEADER_NOCACHE = "no-cache";
    private static final boolean DEFAULT_NOCACHE = true;

    /**
     * 直接输出json串文本.
     *
     * @see #render(response, String, String, String...)
     */
    public static void renderText(HttpServletResponse response, final String jsonText, final String... headers) {
        render(response, TEXT_TYPE, jsonText, headers);
    }

    /**
     * 直接输出内容的简便函数.
     * <p>
     * eg. render(response, "text/plain", "hello", "encoding:GBK"); render(response, "text/plain", "hello",
     * "no-cache:false"); render(response, "text/plain", "hello", "encoding:GBK", "no-cache:false");
     *
     * @param headers 可变的header数组，目前接受的值为"encoding:"或"no-cache:",默认值分别为UTF-8和true.
     */
    public static void render(HttpServletResponse response, final String contentType, final String content, final String... headers) {
        initResponseHeader(response, contentType, headers);
        try {
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 输出json串,响应类型 text/plain
     *
     * @param data    支持：pojo，Set<pojo>,List<pojo>,Map<Object,pojo>
     * @param headers
     */
    public static void renderJson(HttpServletResponse response, Object data, String... headers) {
        render(response, TEXT_TYPE, pojoToJson(data), headers);
    }

    public static void renderJsonp(HttpServletResponse response, Object data, String... headers) {
        render(response, TEXT_TYPE, pojoToJsonp(data), headers);
    }

    public static void renderJsonp(HttpServletResponse response, String callbackName, Object data, String... headers) {
        render(response, TEXT_TYPE, pojoToJsonp(callbackName, data), headers);
    }

    /***
     * @param obj 支持：pojo，Set<pojo>,List<pojo>,Map<Object,pojo>
     * @return
     */
    public static String pojoToJson(Object obj) {
        String json = StringUtils.EMPTY;
        if (obj != null) {
            json = normalMapper.toJson(obj);
        }
        return json;
    }

    /**
     * pojo的非空属性 json输出
     *
     * @param obj
     * @return
     */
    public static String pojoNotEmptyToJson(Object obj) {
        String json = StringUtils.EMPTY;
        if (obj != null) {
            json = JsonMapper.nonEmptyMapper().toJson(obj);
        }
        return json;
    }

    /***
     * @param obj 支持：pojo，Set<pojo>,List<pojo>,Map<Object,pojo>
     * @return jsonp
     */
    public static String pojoToJsonp(Object obj) {
        return pojoToJsonp(CALL_BACK, obj);
    }

    /***
     * @param callbackName 回调参数名
     * @param obj          支持：pojo，Set<pojo>,List<pojo>,Map<Object,pojo>
     * @return jsonp
     */
    public static String pojoToJsonp(String callbackName, Object obj) {
        String json = StringUtils.EMPTY;
        if (obj != null) {
            json = normalMapper.toJsonP(callbackName, obj);
        }
        return json;
    }

    /**
     * 分析并设置contentType与headers.
     */
    private static HttpServletResponse initResponseHeader(HttpServletResponse response, final String contentType, final String... headers) {
        //分析headers参数
        String encoding = Constants.DEFAULT_ENCODING;
        boolean noCache = DEFAULT_NOCACHE;
        if (headers != null && headers.length > 0) {
            for (String header : headers) {
                String headerName = StringUtils.substringBefore(header, ":");
                String headerValue = StringUtils.substringAfter(header, ":");

                if (StringUtils.equalsIgnoreCase(headerName, HEADER_ENCODING)) {
                    encoding = headerValue;
                } else if (StringUtils.equalsIgnoreCase(headerName, HEADER_NOCACHE)) {
                    noCache = Boolean.parseBoolean(headerValue);
                } else {
                    throw new IllegalArgumentException(headerName + "不是一个合法的header类型");
                }
            }
        }
        //设置headers参数
        String fullContentType = contentType + ";charset=" + encoding;
        response.setContentType(fullContentType);
        if (noCache) {
            Servlets.setNoCacheHeader(response);
        }

        return response;
    }

    /**
     * 将POJO转为Map,用于构造ModelAndView
     */
    public static Map<String, Object> toParameterMap(Object parameter) {
        if (parameter instanceof Map) {
            return (Map<String, Object>) parameter;
        } else {
            try {
                return PropertyUtils.describe(parameter);
            } catch (Exception e) {
                org.springframework.util.ReflectionUtils.handleReflectionException(e);
                return null;
            }
        }
    }

    /**
     * 用来将JSON串转为对象 , 支持泛型集合 Collection转换
     */
    public static Object fromJson(String json, JavaType typeOfT) {
        return normalMapper.fromJson(json, typeOfT);
    }

    /**
     * 用来将JSON串转为对象，支持基本的pojo对象转换， 不可用来转带泛型的集合
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return normalMapper.fromJson(json, classOfT);
    }
}