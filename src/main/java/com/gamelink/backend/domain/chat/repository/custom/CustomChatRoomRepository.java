package com.gamelink.backend.domain.chat.repository.custom;

import com.gamelink.backend.domain.chat.model.entity.ChatRoom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomChatRoomRepository {
    List<ChatRoom> findAllChatRoom();

    Optional<ChatRoom> findChatRoomByRoomId(UUID roomId);

    boolean checkChatRoomManagerbyUserSubId(UUID roomId, UUID userSubId);
}
