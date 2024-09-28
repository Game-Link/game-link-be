package com.gamelink.backend.domain.chat.model.dto.request;

import com.gamelink.backend.domain.chat.model.FileType;
import com.gamelink.backend.domain.chat.model.MessageType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class RequestChatDto {

    private final MessageType type;

    private final UUID roomId;

    private final UUID userSubId;

    private final String sender;

    private final String message;

    private final String fileName;

    private final String fileUrl;

    private final FileType fileType;
}
