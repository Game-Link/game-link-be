package com.gamelink.backend.domain.chat.controller;

import com.gamelink.backend.domain.chat.model.dto.Message;
import com.gamelink.backend.domain.chat.model.dto.request.RequestChatDto;
import com.gamelink.backend.domain.chat.service.ChatService;
import com.gamelink.backend.domain.chat.service.MessageSender;
import com.gamelink.backend.domain.chat_message.model.entity.ChatRoomMessage;
import com.gamelink.backend.domain.chat_message.service.ChatRoomMessageService;
import com.gamelink.backend.domain.user.exception.UserNotFoundException;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.domain.user.repository.UserRepository;
import com.gamelink.backend.global.auth.role.UserAuth;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Tag(name = "채팅", description = "채팅 송/수신 관련 api")
@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    @Value("${spring.kafka.consumer.topic}")
    private String topic;

    private final ChatService chatService;
    private final ChatRoomMessageService chatRoomMessageService;
    private final UserRepository userRepository;
    private final MessageSender sender;

    /**
     * 채팅방 별, 입장 이벤트 발생시 처리되는 기능
     *
     * @param request  채팅으로 보낼 메시지 정보 관련 param
     *
     * MessageMapping 을 통해 webSocket 로 들어오는 메시지를 발신 처리한다.
     * 이때 클라이언트에서는 /pub/chat/sendMessage 로 요청하게 되고 이것을 controller 가 받아서 처리한다.
     * 처리가 완료되면 /sub/chatRoom/enter/roomId 로 메시지가 전송된다.
     */
    @MessageMapping("/chat/enterUser")
    public void enterUser(@Payload RequestChatDto request) {

        if (!chatService.alreadyInRoom(request.getRoomId(), request.getUserSubId())) {
            chatService.plusUserCount(request.getRoomId());

            User user = userRepository.findOneBySubId(request.getUserSubId())
                    .orElseThrow(UserNotFoundException::new);

            String nickname = chatService.addUserToRoom(request.getRoomId(), user.getNickname());

            String enterMessage = user.getNickname() + " 님이 입장하였습니다.";
            LocalDateTime messageTime = LocalDateTime.now();

            chatRoomMessageService.create(request.getRoomId(), request.getType().toString(),
                    request.getUserSubId(), user.getNickname(), enterMessage, messageTime, "", "", request.getFileType().toString());

            Message message = Message.builder()
                    .type(request.getType())
                    .roomSubId(request.getRoomId())
                    .sender(nickname)
                    .message(enterMessage)
                    .messageTime(messageTime)
                    .fileType(request.getFileType())
                    .build();
            sender.send(topic, message);
        }
    }

    /**
     * 채팅방 별, 채팅 메시지 전송 기능
     *
     * @param request  채팅으로 보낼 메시지 정보 관련 param
     */
    @MessageMapping("/chat/sendMessage")
    public void sendMessage(@Payload RequestChatDto request) {
        LocalDateTime messageTime = LocalDateTime.now();

        chatRoomMessageService.create(request.getRoomId(), request.getType().toString(),
                request.getUserSubId(), request.getSender(), request.getMessage(), messageTime, request.getFileName(), request.getFileUrl(), request.getFileType().toString());

        Message message = Message.builder()
                .type(request.getType())
                .roomSubId(request.getRoomId())
                .sender(request.getSender())
                .message(request.getMessage())
                .messageTime(messageTime)
                .fileName(request.getFileName())
                .fileUrl(request.getFileUrl())
                .fileType(request.getFileType())
                .build();

        sender.send(topic, message);
    }

    /**
     * 채팅방 별, 퇴장 이벤트 발생시 처리되는 기능
     *
     * 유저 퇴장 시에는 EventListener 을 통해서 유저 퇴장을 확인
     */
//    @EventListener
//    public void webSocketDisconnectListener(SessionDisconnectEvent event) {
//        log.info("DisConnEvent {}", event);
//
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//
//        // stomp 세션에 있던 username과 roomId 를 확인해서 채팅방 유저 리스트와 room 에서 해당 유저를 삭제
//        String username = (String) headerAccessor.getSessionAttributes().get("username");
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//        log.info("퇴장 controller에서 uuid " + username);
//        log.info("퇴장 controller에서 roomId " + roomId);
//
//        log.info("headAccessor {}", headerAccessor);
//
//        // 채팅방 유저 -1
//        chatService.minusUserCnt(roomId);
//
//        // 채팅방 유저 리스트에서 유저 삭제
//        chatService.delUser(roomId, username);
//
//        if (username != null) {
//            log.info("User Disconnected : ", username);
//
//            String exitMessage = username + " 님 퇴장!!";
//            LocalDateTime messageTime = LocalDateTime.now();
//
//            // 퇴장 메시지 저장
//            chatRoomMessageService.create(roomId, MessageType.LEAVE.toString(), userId, username, exitMessage, messageTime);
//            // builder 어노테이션 활용
//            Message message = Message.builder()
//                    .type(MessageType.LEAVE)
//                    .sender(username)
//                    .roomId(roomId)
//                    .message(exitMessage)
//                    .messageTime(messageTime)
//                    .build();
//
//            sender.send(topic, message);
//        }
//    }

    /**
     * 채팅방 별, 채팅에 참여한 유저 리스트 반환
     *
     * @param roomId    채팅방 id
     */
    @GetMapping("/chat/users")
    @UserAuth
    public List<String> getUserList(@RequestParam UUID roomId) {
        return chatService.getUserList(roomId);
    }

    /**
     * 채팅방 별, 이전에 나눈 채팅 메시지 리스트 반환
     *
     * @param roomId    채팅방 id
     */
    @GetMapping("/chat/message/list")
    @UserAuth
    public List<ChatRoomMessage> getChatMessageList(@RequestParam UUID roomId) {
        return chatRoomMessageService.findAllChatRoomMessages(roomId);
    }
}
