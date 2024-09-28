package com.gamelink.backend.domain.chat.service;

import com.gamelink.backend.domain.chat.model.dto.Message;

public interface MessageSender {
    void send(String topic, Message data);
}
