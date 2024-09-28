package com.gamelink.backend.domain.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface MessageReceiver {

    void receiveMessage(String chatMessage) throws JsonProcessingException;
}
