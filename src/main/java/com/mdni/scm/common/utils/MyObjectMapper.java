package com.mdni.scm.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.TimeZone;

/**
 * 对象json序列化和反序列化
 *
 * @author weiys
 */
@SuppressWarnings("all")
@Component
public class MyObjectMapper extends ObjectMapper {
    private static final String br = "<br/>";


    public MyObjectMapper() {
        //json格式化日期 用GMT+8
        setTimeZone(TimeZone.getTimeZone("GMT+8"));
        setSerializationInclusion(JsonInclude.Include.NON_NULL);
        registerModule(new MyModule());
    }

    private static class MyModule extends SimpleModule {

        public MyModule() {

            //将页面传来的string字符串去掉两边的空格
            addDeserializer(String.class, new StdScalarDeserializer<String>(String.class) {
                @Override
                public String deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
                        JsonProcessingException {
                    return StringUtils.trim(jp.getValueAsString());
                }
            });

            //对传到页面到BigDecimal类型数据保留4位小数
            addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
                @Override
                public void serialize(BigDecimal value, JsonGenerator jgen,
                                      SerializerProvider provider) throws IOException,
                        JsonProcessingException {

                    jgen.writeNumber(ArithUtils.round(value.doubleValue(), 4));
                }
            });

            //将<br/> 替换掉
            addSerializer(String.class, new JsonSerializer<String>() {
                @Override
                public void serialize(String value, JsonGenerator jgen,
                                      SerializerProvider provider) throws IOException,
                        JsonProcessingException {

                    jgen.writeString(value.replaceAll("＜br/＞", br));
                }
            });
        }
    }
}

