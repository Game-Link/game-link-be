package com.gamelink.backend.domain.chat.model.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.gamelink.backend.domain.chat.model.FileType;
import com.gamelink.backend.domain.chat.model.MessageType;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@ToString
public class Message {

    @NotNull
    private MessageType type;

    @NotNull
    private UUID roomSubId;

    @NotNull
    private String sender;

    @NotNull
    private String message;

    @NotNull
    private LocalDateTime messageTime;

    private String fileName;

    private String fileUrl;

    private FileType fileType;

    @Builder
    private Message(MessageType type,
                    UUID roomSubId,
                    String sender,
                    String message,
                    LocalDateTime messageTime,
                    String fileName,
                    String fileUrl,
                    FileType fileType) {
        this.type = type;
        this.roomSubId = roomSubId;
        this.sender = sender;
        this.message = message;
        this.messageTime = messageTime;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
        this.fileType = fileType;
    }
}
