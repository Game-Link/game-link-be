package com.gamelink.backend.domain.chat_message.service;

import com.gamelink.backend.domain.chat_message.model.entity.ChatRoomMessage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ChatRoomMessageService {

    void create(UUID roomSubId,
                String messageType,
                UUID userSubId,
                String nickname,
                String content,
                LocalDateTime messageTime,
                String fileName,
                String fileUrl,
                String fileType);

    List<ChatRoomMessage> findAllChatRoomMessages(UUID roomId);

    void deleteChatRoomMessages(UUID roomId);
}
