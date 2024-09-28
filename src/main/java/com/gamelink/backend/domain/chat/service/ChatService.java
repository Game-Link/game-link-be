package com.gamelink.backend.domain.chat.service;

import com.gamelink.backend.domain.chat.model.dto.response.ResponseChatRoomDto;
import com.gamelink.backend.domain.chat.model.entity.ChatRoom;

import java.util.List;
import java.util.UUID;

public interface ChatService {

    List<ResponseChatRoomDto> findAllRoom();

    ResponseChatRoomDto findRoomById(UUID roomSubId);

    ChatRoom createRoom(String roomName, int maxUserCount, UUID userSUbId);

    void plusUserCount(UUID roomSubId);

    void minusUserCount(UUID roomSubId);

    boolean checkRoomUserCount(UUID roomSubId);

    String addUserToRoom(UUID roomSubId, String nickname);

    boolean alreadyInRoom(UUID roomSubId, UUID userSubId);

    void deleteUser(UUID roomSubId, String nickname);

    List<String> getUserList(UUID roomSubId);

    boolean confirmRoomManager(UUID roomSubId, UUID userSubId);

    void deleteChatRoom(UUID roomSubId, UUID userSubId, boolean isAdmin);
}
