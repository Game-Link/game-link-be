package com.gamelink.backend.domain.chat.model.dto.response;

import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

@Getter
public class ResponseChatRoomDto {
    @Schema(description = "채팅방 ID")
    private final UUID roomId;
    @Schema(description = "채팅방 이름")
    private final String roomName;
    @Schema(description = "현재 채팅방 인원 수")
    private final int userCount;
    @Schema(description = "채팅방 최대 인원 수")
    private final int maxUserCount;

    public ResponseChatRoomDto(ChatRoom chatRoom) {
        this.roomId = chatRoom.getSubId();
        this.roomName = chatRoom.getRoomName();
        this.userCount = chatRoom.getUserCount();
        this.maxUserCount = chatRoom.getMaxUserCount();
    }
}
