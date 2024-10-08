package com.gamelink.backend.infra.riot.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.global.config.redis.RedisKeys;
import com.gamelink.backend.global.model.CacheObject;
import com.gamelink.backend.infra.riot.model.dto.response.PersistMatchDataDto;
import com.gamelink.backend.infra.riot.model.dto.response.CacheMatchDataDto;
import com.gamelink.backend.infra.riot.repository.MatchIdCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.gamelink.backend.global.config.redis.RedisKeys.combine;

@Repository
@RequiredArgsConstructor
public class MatchIdCacheRepositoryImpl implements MatchIdCacheRepository {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    public void putMatchId(List<PersistMatchDataDto> matches, UUID riotUserSubId, boolean isNew) {
        String key = combine(RedisKeys.MATCH_ID_KEY, riotUserSubId);

        for(PersistMatchDataDto match : matches) {
            CacheMatchDataDto data = PersistMatchDataDto.convertToCacheMatchDataDto(match);
            CacheObject<CacheMatchDataDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            if (isNew) {
                redisTemplate.opsForList().leftPush(key, value);
            } else {
                redisTemplate.opsForList().rightPush(key, value);
            }
        }
    }

    @Override
    public boolean existMatchId(UUID riotUserSubId) {
        String key = combine(RedisKeys.MATCH_ID_KEY, riotUserSubId);
        Long size = redisTemplate.opsForList().size(key);
        return size != null && size > 0;
    }

    @Override
    public List<CacheMatchDataDto> getMatchId(UUID riotUserSubId, int start) {
        String key = combine(RedisKeys.MATCH_ID_KEY, riotUserSubId);
        int size = redisTemplate.opsForList().size(key).intValue();
        int end = start + 4;
        if (start + 4 > size) {
            end = -1;
        }
        List<String> matches = redisTemplate.opsForList().range(key, start, end);

        List<CacheMatchDataDto> result = new ArrayList<>();

        for (String jsonMatch : matches) {
            try {
                JavaType type = objectMapper.getTypeFactory().constructParametricType(CacheObject.class, CacheMatchDataDto.class);
                CacheObject<CacheMatchDataDto> obj = objectMapper.readValue(jsonMatch, type);
                result.add(obj.getValue());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public CacheMatchDataDto getLastMatchId(UUID riotUserSubId) {
        String key = combine(RedisKeys.MATCH_ID_KEY, riotUserSubId);
        List<String> lastString = redisTemplate.opsForList().range(key, -1, -1);

        try {
            JavaType type = objectMapper.getTypeFactory().constructParametricType(CacheObject.class, CacheMatchDataDto.class);
            CacheObject<CacheMatchDataDto> obj = objectMapper.readValue(lastString.get(0), type);
            return obj.getValue();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
