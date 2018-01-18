package com.mdni.scm.common.utils.cache;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

/**
 * Created by weiys on 16/5/13.
 */

@Component
public class MySimpleKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {

        //service类名加参数作为key 比如FundService+methodname + params
        return new StringBuffer(target.getClass().getSimpleName()).append(".")
                .append(method.getName()).append(".")
                .append(StringUtils.arrayToCommaDelimitedString(params)).toString();
    }

}