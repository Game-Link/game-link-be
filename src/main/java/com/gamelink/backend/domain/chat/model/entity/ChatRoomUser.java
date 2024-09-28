package com.gamelink.backend.domain.chat.model.entity;

import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room_user")
public class ChatRoomUser extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private User participant;

    @Builder
    private ChatRoomUser(ChatRoom chatRoom, User user) {
        this.participant = user;
        this.chatRoom = chatRoom;
    }

    public static ChatRoomUser convertFromParameters(ChatRoom chatRoom, User user) {
        return new ChatRoomUser(
                chatRoom,
                user
        );
    }
}
