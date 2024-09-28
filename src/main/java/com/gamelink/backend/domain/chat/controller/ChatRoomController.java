package com.gamelink.backend.domain.chat.controller;

import com.gamelink.backend.domain.chat.model.dto.response.ResponseChatRoomDto;
import com.gamelink.backend.domain.chat.model.dto.response.ResponseCreateChatroom;
import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import com.gamelink.backend.domain.chat.service.ChatService;
import com.gamelink.backend.domain.chat_message.service.ChatRoomMessageService;
import com.gamelink.backend.global.auth.jwt.AppAuthentication;
import com.gamelink.backend.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "채팅방", description = "채팅방 관련 api")
@RestController
@RequestMapping("/chatroom")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatService chatService;
    private final ChatRoomMessageService chatroomMessageService;

    /**
     * 채팅방 목록 조회
     *
     * <p>모든 채팅방 목록을 조회합니다.</p>
     */
    @GetMapping
    @UserAuth
    public List<ResponseChatRoomDto> getChatroomList() {
        return chatService.findAllRoom();
    }

    /**
     * 채팅방 생성
     *
     * <p>채팅방을 생성합니다.</p>
     */
    @PostMapping("/create")
    @UserAuth
    public ResponseCreateChatroom createRoom(@RequestParam String roomName,
                                             @RequestParam(value = "maxUserCount", defaultValue = "10") int maxUserCount,
                                             AppAuthentication auth) {
        ChatRoom room = chatService.createRoom(roomName, maxUserCount, UUID.fromString(auth.getUserSubId()));
        return new ResponseCreateChatroom(room);
    }

    /**
     * 채팅방 방장 확인
     *
     * @param auth     사용자 인증 정보
     * @param roomId   채팅방 Id
     */
    @GetMapping("/confirm/manager/{roomId}")
    @UserAuth
    public boolean confirmManager(AppAuthentication auth,
                                  @PathVariable UUID roomId) {
        return chatService.confirmRoomManager(roomId, UUID.fromString(auth.getUserSubId()));
    }

    /**
     * 채팅방 삭제
     *
     * @param roomId    채팅방 Id
     */
    @DeleteMapping
    @UserAuth
    public boolean removeRoom(@RequestParam UUID roomId, AppAuthentication auth) {
        // TODO : 채팅방에 존재하는 파일들 삭제 로직 추가
        chatroomMessageService.deleteChatRoomMessages(roomId);
        chatService.deleteChatRoom(UUID.fromString(auth.getUserSubId()), roomId, auth.isAdmin());
        return true;
    }

    /**
     * maxUserCount에 따른 채팅방 입장 여부
     *
     * @param roomId    채팅방 Id
     */
    @GetMapping("/checkUserCnt/{roomId}")
    public boolean chkUserCnt(@PathVariable UUID roomId) {
        return chatService.checkRoomUserCount(roomId);
    }

}
