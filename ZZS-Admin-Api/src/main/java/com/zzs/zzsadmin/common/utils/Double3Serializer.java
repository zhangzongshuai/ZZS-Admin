package com.zzs.zzsadmin.common.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class Double3Serializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (o != null) {
            BigDecimal bg = new BigDecimal(o.toString()).setScale(3, RoundingMode.UP);
            jsonGenerator.writeString(bg.toString());
        }
    }
}
