package com.gamelink.backend.domain.chat.model.dto.response;

import com.gamelink.backend.domain.chat.model.ChatRoomStatus;
import com.gamelink.backend.domain.chat.model.entity.ChatRoom;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ResponseCreateChatroom {

    @Schema(description = "채팅방 이름", example = "채팅방")
    private final String roomName;

    @Schema(description = "최대 인원 수", example = "10")
    private final int maxUserCount;

    @Schema(description = "현재 인원 수", example = "0")
    private final int userCount;

    @Schema(description = "채팅방 상태", example = "ACTIVE")
    private final ChatRoomStatus status;

    public ResponseCreateChatroom(ChatRoom room) {
        this.roomName = room.getRoomName();
        this.maxUserCount = room.getMaxUserCount();
        this.userCount = room.getUserCount();
        this.status = room.getChatRoomStatus();
    }
}
