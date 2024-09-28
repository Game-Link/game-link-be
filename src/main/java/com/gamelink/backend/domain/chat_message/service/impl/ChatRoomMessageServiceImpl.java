package com.gamelink.backend.domain.chat_message.service.impl;

import com.gamelink.backend.domain.chat_message.model.entity.ChatRoomMessage;
import com.gamelink.backend.domain.chat_message.repository.ChatRoomMessageRepository;
import com.gamelink.backend.domain.chat_message.service.ChatRoomMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatRoomMessageServiceImpl implements ChatRoomMessageService {

    private final ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");

    private final ChatRoomMessageRepository chatRoomMessageRepository;

    @Override
    public void create(UUID roomSubId, String messageType, UUID userSubId, String nickname, String content, LocalDateTime messageTime, String fileName, String fileUrl, String fileType) {
        ChatRoomMessage chatRoomMessage = new ChatRoomMessage();
        chatRoomMessage.setRoomSubId(roomSubId);
        chatRoomMessage.setMessageType(messageType);
        chatRoomMessage.setUserSubId(userSubId);
        chatRoomMessage.setNickname(nickname);
        chatRoomMessage.setContent(content);
        chatRoomMessage.setCreatedAt(messageTime.atZone(seoulZoneId).toLocalDateTime());
        chatRoomMessage.setFileName(fileName);
        chatRoomMessage.setFileUrl(fileUrl);
        chatRoomMessage.setFileType(fileType);

        chatRoomMessageRepository.save(chatRoomMessage);
    }

    @Override
    public List<ChatRoomMessage> findAllChatRoomMessages(UUID roomId) {
        return chatRoomMessageRepository.findAllByRoomSubIdOrderByCreatedAtAsc(roomId);
    }

    @Override
    public void deleteChatRoomMessages(UUID roomId) {
        chatRoomMessageRepository.deleteAllByRoomSubId(roomId);
    }
}
