package cn.damei.scm.common.utils.springmvc;

import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by weiys on 16/5/18. MapRequestArgument注解处理类
 */
public class MapRequestArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //System.out.println(parameter.get);
        return parameter.getParameterAnnotation(MapRequestArgument.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, Object> otherParams = Maps.newHashMap();

        Object value = null;
        for (Iterator<String> paramNames = webRequest.getParameterNames(); paramNames.hasNext(); ) {
            String paramName = paramNames.next();
            if (paramName.contains("otherParams")) {
                value = webRequest.getParameter(paramName);
                if (value != null && StringUtils.isNotBlank(value.toString())) {
                    otherParams.put(paramName.substring(paramName.indexOf("[") + 1, paramName.indexOf("]")), value);
                }
            }
        }

        return otherParams.isEmpty() ? null : otherParams;
    }
}
