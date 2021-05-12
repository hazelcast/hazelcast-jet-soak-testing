package com.hazelcast.jet.tests.sql.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.jet.tests.sql.pojo.Pojo;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class PojoSerializer implements Serializer<Pojo> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Pojo data) {
        byte[] result = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.writeValueAsString(data).getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void close() {
    }
}