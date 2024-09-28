package com.gamelink.backend.domain.chat.service.impl;

import com.gamelink.backend.domain.chat.exception.ChatRoomNotFoundException;
import com.gamelink.backend.domain.chat.model.dto.response.ResponseChatRoomDto;
import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import com.gamelink.backend.domain.chat.model.entity.ChatRoomUser;
import com.gamelink.backend.domain.chat.repository.ChatRoomRepository;
import com.gamelink.backend.domain.chat.repository.ChatRoomUserRepository;
import com.gamelink.backend.domain.chat.service.ChatService;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.global.error.exception.NotGrantedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    /**
     * 전체 채팅방 조회
     *
     * @return     각 채팅방 정보 list
     */
    @Override
    public List<ResponseChatRoomDto> findAllRoom() {
        List<ChatRoom> roomList = chatRoomRepository.findAllChatRoom();
        return roomList.stream().map(ResponseChatRoomDto::new).toList();
    }

    /**
     * roomID를 기준으로 채팅방 조회
     *
     * @param roomSubId      찾고자 하는 채팅방 id
     * @return            채팅방 정보
     */
    @Override
    public ResponseChatRoomDto findRoomById(UUID roomSubId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        return new ResponseChatRoomDto(chatRoom);
    }

    /**
     * 채팅방 생성
     *
     * @param roomName        채팅방 이름
     * @param maxUserCount    최대 인원 수
     * @param userSubId       채팅방 생성자 subId
     */
    @Override
    @Transactional
    public ChatRoom createRoom(String roomName, int maxUserCount, UUID userSubId) {
        User user = userRepository.findOneBySubId(userSubId).orElseThrow(UserNotFoundException::new);

        ChatRoom chatRoom = ChatRoom.convertFromParameters(roomName, maxUserCount, user);
        return chatRoomRepository.save(chatRoom);
    }

    /**
     * 채팅방 인원 +1
     *
     * @param roomSubId    채팅방 id
     */
    @Override
    @Transactional
    public void plusUserCount(UUID roomSubId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.plusUserCount();
    }

    /**
     * 채팅방 인원 -1
     *
     * @param roomSubId    채팅방 id
     */
    @Override
    @Transactional
    public void minusUserCount(UUID roomSubId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        chatRoom.minusUserCount();
    }

    /**
     * maxUserCount에 따른 채팅방 입장 여부
     *
     * @param roomSubId    채팅방 id
     */
    @Override
    public boolean checkRoomUserCount(UUID roomSubId) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        return chatRoom.getUserCount() < chatRoom.getMaxUserCount();
    }

    /**
     * 채팅방 유저 리스트에 유저 추가
     *
     * @param roomSubId       채팅방 id
     * @param nickname     유저 닉네임
     */
    @Override
    @Transactional
    public String addUserToRoom(UUID roomSubId, String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);

        ChatRoomUser newChatUser = ChatRoomUser.convertFromParameters(chatRoom, user);
        chatRoomUserRepository.save(newChatUser);

        return user.getNickname();
    }

    /**
     * 특정 채팅방에 참여중인 유저인지 확인
     */
    @Override
    public boolean alreadyInRoom(UUID roomSubId, UUID userSubId) {
        UUID chatRoomId = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new).getSubId();
        return chatRoomUserRepository.existsUserByRoomIdAndUserSubId(chatRoomId, userSubId);
    }

    @Override
    @Transactional
    public void deleteUser(UUID roomSubId, String nickname) {
        User user = userRepository.findByNickname(nickname)
                .orElseThrow(UserNotFoundException::new);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        chatRoomUserRepository.deleteChatRoomUserByRoomIdAndUserSubId(chatRoom.getSubId(), user.getSubId());
    }

    @Override
    public List<String> getUserList(UUID roomSubId) {
        return chatRoomUserRepository.findAllChatRoomUserByRoomId(roomSubId);
    }

    @Override
    public boolean confirmRoomManager(UUID roomSubId, UUID userSubId) {
        return chatRoomRepository.checkChatRoomManagerbyUserSubId(roomSubId, userSubId);
    }

    @Override
    @Transactional
    public void deleteChatRoom(UUID roomSubId, UUID userSubId, boolean isAdmin) {
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByRoomId(roomSubId)
                .orElseThrow(ChatRoomNotFoundException::new);
        if (isAdmin) {
            chatRoom.markAsDeleted(true);
        } else if (chatRoom.getRoomAdmin().getSubId().equals(userSubId)) {
            chatRoom.markAsDeleted(false);
        } else {
            throw new NotGrantedException();
        }
    }
}
