package com.gamelink.backend.domain.chat.repository.custom;

import com.gamelink.backend.domain.chat.model.entity.ChatRoomUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomChatRoomUserRepository {
    boolean existsUserByRoomIdAndUserSubId(UUID roomSubId, UUID userSubId);

    List<String> findAllChatRoomUserByRoomId(UUID roomSubId);

    void deleteChatRoomUserByRoomIdAndUserSubId(UUID roomSubId, UUID userSubId);
}
