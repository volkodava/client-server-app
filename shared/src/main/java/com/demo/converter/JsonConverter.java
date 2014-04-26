package com.demo.converter;

import java.io.IOException;
import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Service;

@Service("messageConverter")
public class JsonConverter {

    private final ObjectMapper mapper = new ObjectMapper();

    public <T> T readValue(String text, Class<T> clazz) throws IOException {

        Validate.notNull(clazz, "Class must not be null");

        T resultObject = mapper.readValue(text, clazz);
        return resultObject;
    }

    public <T> String writeValueAsString(T object) throws IOException {

        String resultText = mapper.writeValueAsString(object);
        return resultText;
    }
}
