package com.gamelink.backend.domain.chat_message.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.gamelink.backend.global.config.aws.DynamoDBConfig;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ChatRoomMessageId implements Serializable {
    private static final long serialVersionUID = 1L;

    @DynamoDBHashKey
    private UUID roomSubId;

    @DynamoDBRangeKey
    @DynamoDBTypeConverted(converter = DynamoDBConfig.LocalDateTimeConverter.class)
    private LocalDateTime createdAt;

}
