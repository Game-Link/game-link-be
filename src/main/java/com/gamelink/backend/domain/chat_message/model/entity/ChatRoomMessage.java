package com.gamelink.backend.domain.chat_message.model.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.gamelink.backend.domain.chat_message.model.ChatRoomMessageId;
import com.gamelink.backend.global.config.DynamoDBConfig;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

@DynamoDBTable(tableName = "ChatRoomMessage")
@Getter
@Setter
@NoArgsConstructor()
public class ChatRoomMessage {

    @Id
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private ChatRoomMessageId chatRoomMessageId;

    @DynamoDBHashKey(attributeName = "roomSubId")
    public UUID getRoomSubId() {
        return chatRoomMessageId != null ? chatRoomMessageId.getRoomSubId() : null;
    }

    public void setRoomSubId(UUID roomId) {
        if (chatRoomMessageId == null) {
            chatRoomMessageId = new ChatRoomMessageId();
        }
        chatRoomMessageId.setRoomSubId(roomId);
    }

    @DynamoDBRangeKey(attributeName = "createdAt")
    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    public LocalDateTime getCreatedAt() {
        return chatRoomMessageId != null ? chatRoomMessageId.getCreatedAt() : null;
    }

    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    public void setCreatedAt(LocalDateTime createdAt) {
        if (chatRoomMessageId == null) {
            chatRoomMessageId = new ChatRoomMessageId();
        }
        chatRoomMessageId.setCreatedAt(createdAt);
    }

    @DynamoDBAttribute
    private String messageType;

    @DynamoDBAttribute
    private UUID userSubId;

    @DynamoDBAttribute
    private String nickname;

    @DynamoDBAttribute
    private String content;

    @DynamoDBAttribute
    private String fileName;

    @DynamoDBAttribute
    private String fileUrl;

    @DynamoDBAttribute
    private String fileType;
}
