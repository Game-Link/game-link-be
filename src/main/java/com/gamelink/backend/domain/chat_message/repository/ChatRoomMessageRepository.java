package com.gamelink.backend.domain.chat_message.repository;

import com.gamelink.backend.domain.chat_message.model.ChatRoomMessageId;
import com.gamelink.backend.domain.chat_message.model.entity.ChatRoomMessage;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@EnableScan
@Repository
public interface ChatRoomMessageRepository extends CrudRepository<ChatRoomMessage, ChatRoomMessageId> {
    List<ChatRoomMessage> findAllByRoomSubIdOrderByCreatedAtAsc(UUID roomId);

    void deleteAllByRoomSubId(UUID roomId);

    List<ChatRoomMessage> findAllByRoomSubIdAndFileType(UUID roomId, String fileType);
}
