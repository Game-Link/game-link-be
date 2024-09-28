package com.gamelink.backend.domain.chat.exception;

import com.gamelink.backend.global.error.exception.LocalizedMessageException;
import org.springframework.http.HttpStatus;

public class ChatRoomNotFoundException extends LocalizedMessageException {

    public ChatRoomNotFoundException() {
        super(HttpStatus.NOT_FOUND, "notfound.chatroom");
    }
}
