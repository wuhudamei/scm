package cn.damei.scm.common.utils.cache;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.damei.scm.common.utils.MyObjectMapper;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Created by weiys on 16/5/14.
 */
@Component
public class MyJackson2JsonRedisSerializer implements RedisSerializer<Object> {

    static final byte[] EMPTY_ARRAY = new byte[0];
    private final ObjectMapper mapper;

    /**
     * Creates {@link MyJackson2JsonRedisSerializer} and configures {@link ObjectMapper} for default typing.
     */
    public MyJackson2JsonRedisSerializer() {
        this((String) null);
    }

    /**
     * Creates {@link MyJackson2JsonRedisSerializer} and configures {@link ObjectMapper} for default typing using the
     * given {@literal name}. In case of an {@literal empty} or {@literal null} String the default
     *
     * @param classPropertyTypeName Name of the JSON property holding type information. Can be {@literal null}.
     */
    public MyJackson2JsonRedisSerializer(String classPropertyTypeName) {

        this(new MyObjectMapper());

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        if (StringUtils.hasText(classPropertyTypeName)) {
            mapper.enableDefaultTypingAsProperty(ObjectMapper.DefaultTyping.NON_FINAL, classPropertyTypeName);
        } else {
            mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        }
    }

    /**
     * Setting a custom-configured {@link ObjectMapper} is one way to take further control of the JSON serialization
     * process. For example, an extended {@link } can be configured that provides custom serializers for
     * specific types.
     *
     * @param mapper must not be {@literal null}.
     */
    public MyJackson2JsonRedisSerializer(ObjectMapper mapper) {

        Assert.notNull(mapper, "ObjectMapper must not be null!");
        this.mapper = mapper;
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(Object source) throws SerializationException {

        if (source == null) {
            return EMPTY_ARRAY;
        }

        try {
            return mapper.writeValueAsBytes(source);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Could not write JSON: " + e.getMessage(), e);
        }
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
     */
    @Override
    public Object deserialize(byte[] source) throws SerializationException {
        return deserialize(source, Object.class);
    }

    /**
     * @param source can be {@literal null}.
     * @param type   must not be {@literal null}.
     * @return {@literal null} for empty source.
     * @throws SerializationException
     */
    public <T> T deserialize(byte[] source, Class<T> type) throws SerializationException {

        Assert.notNull(type,
                "Deserialization type must not be null! Pleaes provide Object.class to make use of Jackson2 default typing.");

        if (source == null || source.length == 0) {
            return null;
        }

        try {
            return mapper.readValue(source, type);
        } catch (Exception ex) {
            throw new SerializationException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
}