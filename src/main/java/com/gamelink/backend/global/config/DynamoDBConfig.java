package com.gamelink.backend.global.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Configuration
@EnableDynamoDBRepositories(basePackages = {"com.gamelink.backend.domain.chat_message.repository"},
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class DynamoDBConfig {

    @Value("${aws.credentials.access-key}")
    private String accessKey;

    @Value("${aws.credentials.secret-key}")
    private String secretKey;

    @Value("${aws.credentials.region}")
    private String region;

    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(accessKey, secretKey);
    }

    public AWSCredentialsProvider amazonAWSCredentialProvider() {
        return new AWSStaticCredentialsProvider(amazonAWSCredentials());
    }

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        return AmazonDynamoDBClientBuilder.standard().withCredentials(amazonAWSCredentialProvider())
                .withRegion(region).build();
    }

    /**
     * Java DynamoDB SDK가 Java의 기본 Date 타입만 허용하므로
     * LocalDateTimeType과 Date를 상호 변환할 수 있는 컨버터 추가
     */
    public static class LocalDateTimeConverter implements DynamoDBTypeConverter<Date, LocalDateTime> {
        @Override
        public Date convert(LocalDateTime source) {
            ZoneId seoulZoneId = ZoneId.of("Asia/Seoul");
            return Date.from(source.atZone(seoulZoneId).toInstant());
        }

        @Override
        public LocalDateTime unconvert(Date source) {
            return source.toInstant().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime();
        }
    }
}
