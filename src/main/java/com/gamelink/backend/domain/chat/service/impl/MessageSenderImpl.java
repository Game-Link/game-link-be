package com.gamelink.backend.domain.chat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gamelink.backend.domain.chat.model.dto.Message;
import com.gamelink.backend.domain.chat.service.MessageSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageSenderImpl implements MessageSender {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, Message data) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            String chatMessage = objectMapper.writeValueAsString(data);
            log.info("MessageSenderImpl Message -> String형 : " + chatMessage);
            kafkaTemplate.send(topic, chatMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
