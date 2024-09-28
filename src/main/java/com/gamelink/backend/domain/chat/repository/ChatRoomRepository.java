package com.gamelink.backend.domain.chat.repository;

import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import com.gamelink.backend.domain.chat.repository.custom.CustomChatRoomRepository;
import com.gamelink.backend.global.base.CustomJpaRepository;

public interface ChatRoomRepository extends CustomJpaRepository<ChatRoom, Long>, CustomChatRoomRepository {
}
