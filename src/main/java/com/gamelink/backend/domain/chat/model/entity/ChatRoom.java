package com.gamelink.backend.domain.chat.model.entity;

import com.gamelink.backend.domain.chat.model.ChatRoomStatus;
import com.gamelink.backend.domain.user.model.entity.User;
import com.gamelink.backend.global.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "chat_room")
public class ChatRoom extends BaseEntity {

    @NotNull
    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_manager_id")
    private User roomAdmin;

    @NotNull
    private int userCount;

    @NotNull
    private int maxUserCount;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoomUser> users = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private ChatRoomStatus chatRoomStatus;

    @Builder
    public ChatRoom(String roomName, User roomAdmin, int userCount, int maxUserCount) {
        this.roomName = roomName;
        this.roomAdmin = roomAdmin;
        this.userCount = userCount;
        this.maxUserCount = maxUserCount;
        this.chatRoomStatus = ChatRoomStatus.ACTIVE;
    }

    public static ChatRoom convertFromParameters(String roomName, int maxUserCount, User user) {
        return new ChatRoom(
                roomName,
                user, // 채팅방 방장
                0, // 채팅방 참여 인원수
                maxUserCount // 최대 인원 수 제한
        );
    }

    public void plusUserCount() {
        this.userCount++;
    }

    public void minusUserCount() {
        this.userCount--;
    }

    public void markAsDeleted(boolean byAdmin) {
        this.chatRoomStatus = byAdmin ? ChatRoomStatus.DELETED_BY_ADMIN : ChatRoomStatus.DELETED;
    }
}
