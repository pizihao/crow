package com.deep.crow.jackson;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.IOException;
import java.util.Objects;

/**
 * <h2>获取{@link ObjectMapper}</h2>
 *
 * @author Create by liuwenhao on 2022/4/24 13:28
 */
public class ObjectMapperFactory {

    static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(Long.class, new LongDeserializer());
        objectMapper.registerModule(simpleModule);

    }

    private ObjectMapperFactory() {

    }

    public static void setObjectMapper(ObjectMapper objectMapper) {
        ObjectMapperFactory.objectMapper = objectMapper;
    }

    public static ObjectMapper get() {
        return objectMapper;
    }

    private static class LongSerializer extends JsonSerializer<Long> {
        String str = "Long";
        String separator = ":";

        @Override
        public void serialize(Long value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(str + separator + value);
        }

        @Override
        public void serializeWithType(Long value, JsonGenerator g, SerializerProvider provider,
                                      TypeSerializer typeSer) throws IOException {
            WritableTypeId typeIdDef = typeSer.writeTypePrefix(g, typeSer.typeId(value, JsonToken.VALUE_STRING));
            serialize(value, g, provider);
            typeSer.writeTypeSuffix(g, typeIdDef);
        }
    }

    private static class LongDeserializer extends JsonDeserializer<Long>{

        String start = "[";
        String type = "java.lang.Long";
        String end = "]";

        @Override
        public Long deserialize(JsonParser p, DeserializationContext context) throws IOException, JacksonException {
            String text = p.getText();
            System.out.println(text);
            if (Objects.isNull(text) || text.equals(start) || text.equals(type) || text.equals(end)){
                return null;
            }
            return Long.parseLong(text);
        }
    }
}