package com.gamelink.backend.domain.chat.repository.custom.impl;

import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import com.gamelink.backend.domain.chat.repository.custom.CustomChatRoomRepository;
import com.gamelink.backend.domain.user.exception.NotSingleResultException;
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
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {

    private final EntityManager entityManager;

    @Override
    public List<ChatRoom> findAllChatRoom() {
        return entityManager.createQuery(
                "select cr from ChatRoom cr where cr.chatRoomStatus = 'ACTIVE' " +
                        "order by cr.createdAt DESC",
                ChatRoom.class
        )
                .getResultList();
    }

    @Override
    public Optional<ChatRoom> findChatRoomByRoomId(UUID roomId) {
        List<ChatRoom> chatRoomList = entityManager.createQuery(
                "select cr from ChatRoom cr where cr.subId = :roomId and cr.chatRoomStatus = 'ACTIVE'",
                ChatRoom.class
        )
                .setParameter("roomId", roomId)
                .getResultList();

        if (chatRoomList.isEmpty()) {
            return Optional.empty();
        } else if (chatRoomList.size() > 1) {
            throw new NotSingleResultException();
        } else {
            return Optional.of(chatRoomList.get(0));
        }
    }

    @Override
    public boolean checkChatRoomManagerbyUserSubId(UUID roomId, UUID userSubId) {
        return entityManager.createQuery(
                "select cr from ChatRoom cr where " +
                        "cr.subId = :roomId and cr.roomAdmin.subId = :userSubId",
                Boolean.class
        )
                .setParameter("roomId", roomId)
                .setParameter("userSubId", userSubId)
                .getSingleResult();
    }
}
