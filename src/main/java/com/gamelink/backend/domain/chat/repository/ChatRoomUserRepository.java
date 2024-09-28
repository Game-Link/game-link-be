package com.gamelink.backend.domain.chat.repository;

import com.gamelink.backend.domain.chat.model.entity.ChatRoomUser;
import com.gamelink.backend.domain.chat.repository.custom.CustomChatRoomUserRepository;
import com.gamelink.backend.global.base.CustomJpaRepository;

public interface ChatRoomUserRepository extends CustomJpaRepository<ChatRoomUser, Long>, CustomChatRoomUserRepository {
}
