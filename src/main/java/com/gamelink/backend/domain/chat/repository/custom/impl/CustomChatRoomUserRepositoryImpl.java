package com.gamelink.backend.domain.chat.repository.custom.impl;

import com.gamelink.backend.domain.chat.model.entity.ChatRoomUser;
import com.gamelink.backend.domain.chat.repository.custom.CustomChatRoomUserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class CustomChatRoomUserRepositoryImpl implements CustomChatRoomUserRepository {

    private final EntityManager entityManager;

    @Override
    public boolean existsUserByRoomIdAndUserSubId(UUID roomSubId, UUID userSubId) {
        List<Boolean> results = entityManager.createQuery(
                "select cru from ChatRoomUser cru where " +
                        "cru.chatRoom.subId = :chatRoomId and " +
                        "cru.participant.subId = :userSubId", Boolean.class
        )
                .setParameter("chatRoomId", roomSubId)
                .setParameter("userSubId", userSubId)
                .getResultList();

        return !results.isEmpty();
    }

    @Override
    public List<String> findAllChatRoomUserByRoomId(UUID roomSubId) {
        return entityManager.createQuery(
                "select cru.participant.nickname from ChatRoomUser cru " +
                        "where cru.chatRoom.subId = :roomId", String.class
        )
                .setParameter("roomId", roomSubId)
                .getResultList();
    }

    @Override
    public void deleteChatRoomUserByRoomIdAndUserSubId(UUID roomSubId, UUID userSubId) {
        entityManager.createQuery(
                "delete from ChatRoomUser cru where " +
                        "cru.chatRoom.subId = :chatRoomSubId and " +
                        "cru.participant.subId = :userSubId"
        )
                .setParameter("chatRoomSubId", roomSubId)
                .setParameter("userSubId", userSubId)
                .executeUpdate();
    }
}
