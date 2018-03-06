package cn.damei.scm.shiro;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.poi.ss.formula.functions.T;
import org.apache.shiro.codec.Base64;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class SerializeObjectUtil extends SerializationUtils {

    public static String serializeToString(Serializable obj) {
        try {
            byte[] value = serialize(obj);
            return Base64.encodeToString(value);
        } catch (Exception e) {
            throw new RuntimeException("serialize session error", e);
        }
    }

    public static Object deserializeFromString(String base64SessionStr) {
        try {
            byte[] objectData = Base64.decode(base64SessionStr);
            return deserialize(objectData);
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        }
    }

    public static Collection<Object> deserializeFromStringCollection(Collection<String> base64SessionStrs) {
        try {
            List<Object> list = Lists.newLinkedList();
            for (String base64 : base64SessionStrs) {
                byte[] objectData = Base64.decode(base64);
                T t = deserialize(objectData);
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException("deserialize session error", e);
        }
    }
}