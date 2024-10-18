package com.gamelink.backend.infra.riot.repository.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gamelink.backend.global.config.redis.RedisKeys;
import com.gamelink.backend.global.model.CacheObject;
import com.gamelink.backend.infra.riot.model.dto.CacheChampionDto;
import com.gamelink.backend.infra.riot.model.dto.CacheKdaDto;
import com.gamelink.backend.infra.riot.model.dto.PersistChampionDto;
import com.gamelink.backend.infra.riot.model.dto.PersistKdaDto;
import com.gamelink.backend.infra.riot.model.dto.response.PersistMatchDataDto;
import com.gamelink.backend.infra.riot.model.dto.response.CacheMatchDataDto;
import com.gamelink.backend.infra.riot.repository.MatchIdCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    public void putRankMatchId(List<PersistMatchDataDto> matches, UUID riotUserSubId, boolean isNew, boolean isSolo) {
        String key = combine(isSolo ? RedisKeys.SOLO_RANK_MATCH_ID_KEY : RedisKeys.TEAM_RANK_MATCH_ID_KEY, riotUserSubId);

        for (PersistMatchDataDto match : matches) {
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

    @Override
    public void putKdaAndChampionInfo(PersistKdaDto totalKdaDto, PersistKdaDto soloKdaDto, PersistKdaDto teamKdaDto, List<PersistChampionDto> totalChampionDto, List<PersistChampionDto> soloChampionDto, List<PersistChampionDto> teamChampionDto, UUID riotUserSubId) {
        String totalKdaKey = combine(RedisKeys.TOTAL_KDA_KEY, riotUserSubId);
        String soloKdaKey = combine(RedisKeys.SOLO_KDA_KEY, riotUserSubId);
        String teamKdaKey = combine(RedisKeys.TEAM_KDA_KEY, riotUserSubId);
        String totalChampionKey = combine(RedisKeys.TOTAL_CHAMPION_KEY, riotUserSubId);
        String soloChampionKey = combine(RedisKeys.SOLO_CHAMPION_KEY, riotUserSubId);
        String teamChampionKey = combine(RedisKeys.TEAM_CHAMPION_KEY, riotUserSubId);

        try {
            redisTemplate.opsForValue().set(totalKdaKey, objectMapper.writeValueAsString(totalKdaDto));
            redisTemplate.opsForValue().set(soloKdaKey, objectMapper.writeValueAsString(soloKdaDto));
            redisTemplate.opsForValue().set(teamKdaKey, objectMapper.writeValueAsString(teamKdaDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        for(PersistChampionDto champion : totalChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(totalChampionKey, value);
        }

        for(PersistChampionDto champion : soloChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(soloChampionKey, value);
        }

        for (PersistChampionDto champion : teamChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(teamChampionKey, value);
        }
    }

    @Override
    public CacheKdaDto getTotalKda(UUID riotUserSubId) {
        String key = combine(RedisKeys.TOTAL_KDA_KEY, riotUserSubId);
        String value = redisTemplate.opsForValue().get(key);
        CacheKdaDto totalKdaDto;

        try {
            totalKdaDto = objectMapper.readValue(value, CacheKdaDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return totalKdaDto;
    }

    @Override
    public CacheKdaDto getSoloKda(UUID riotUserSubId) {
        String key = combine(RedisKeys.SOLO_KDA_KEY, riotUserSubId);
        String value = redisTemplate.opsForValue().get(key);
        CacheKdaDto soloKdaDto;

        try {
            soloKdaDto = objectMapper.readValue(value, CacheKdaDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return soloKdaDto;
    }

    @Override
    public CacheKdaDto getTeamKda(UUID riotUserSubId) {
        String key = combine(RedisKeys.TEAM_KDA_KEY, riotUserSubId);
        String value = redisTemplate.opsForValue().get(key);
        CacheKdaDto teamKdaDto;

        try {
            teamKdaDto = objectMapper.readValue(value, CacheKdaDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return teamKdaDto;
    }

    @Override
    public void deleteAndUpdateKda(PersistKdaDto totalKdaDto, PersistKdaDto soloKdaDto, PersistKdaDto teamKdaDto, UUID riotUserSubId) {
        String totalKdaKey = combine(RedisKeys.TOTAL_KDA_KEY, riotUserSubId);
        String soloKdaKey = combine(RedisKeys.SOLO_KDA_KEY, riotUserSubId);
        String teamKdaKey = combine(RedisKeys.TEAM_KDA_KEY, riotUserSubId);

        try {
            redisTemplate.opsForValue().getAndDelete(totalKdaKey);
            redisTemplate.opsForValue().getAndDelete(soloKdaKey);
            redisTemplate.opsForValue().getAndDelete(teamKdaKey);

            redisTemplate.opsForValue().set(totalKdaKey, objectMapper.writeValueAsString(totalKdaDto));
            redisTemplate.opsForValue().set(soloKdaKey, objectMapper.writeValueAsString(soloKdaDto));
            redisTemplate.opsForValue().set(teamKdaKey, objectMapper.writeValueAsString(teamKdaDto));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAndUpdateChampion(List<PersistChampionDto> totalChampionDto, List<PersistChampionDto> soloChampionDto, List<PersistChampionDto> teamChampionDto, UUID riotUserSubId) {
        String totalChampionKey = combine(RedisKeys.TOTAL_CHAMPION_KEY, riotUserSubId);
        String soloChampionKey = combine(RedisKeys.SOLO_CHAMPION_KEY, riotUserSubId);
        String teamChampionKey = combine(RedisKeys.TEAM_CHAMPION_KEY, riotUserSubId);

        redisTemplate.opsForList().remove(totalChampionKey, 0, -1);
        redisTemplate.opsForList().remove(soloChampionKey, 0, -1);
        redisTemplate.opsForList().remove(teamChampionKey, 0, -1);

        for(PersistChampionDto champion : totalChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(totalChampionKey, value);
        }

        for(PersistChampionDto champion : soloChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(soloChampionKey, value);
        }

        for (PersistChampionDto champion : teamChampionDto) {
            CacheChampionDto data = PersistChampionDto.of(champion);
            CacheObject<CacheChampionDto> obj = new CacheObject<>(data);
            String value;
            try {
                value = objectMapper.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            redisTemplate.opsForList().leftPush(teamChampionKey, value);
        }
    }

    @Override
    public List<CacheChampionDto> getTotalChampion(UUID riotUserSubId) {
        String key = combine(RedisKeys.TOTAL_CHAMPION_KEY, riotUserSubId);
        List<String> champions = redisTemplate.opsForList().range(key, 0, -1);

        List<CacheChampionDto> result = new ArrayList<>();

        for (String jsonChampion : champions) {
            try {
                JavaType type = objectMapper.getTypeFactory().constructParametricType(CacheObject.class, CacheChampionDto.class);
                CacheObject<CacheChampionDto> obj = objectMapper.readValue(jsonChampion, type);
                result.add(obj.getValue());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<CacheChampionDto> getSoloChampion(UUID riotUserSubId) {
        String key = combine(RedisKeys.SOLO_CHAMPION_KEY, riotUserSubId);
        List<String> champions = redisTemplate.opsForList().range(key, 0, -1);

        List<CacheChampionDto> result = new ArrayList<>();

        for (String jsonChampion : champions) {
            try {
                JavaType type = objectMapper.getTypeFactory().constructParametricType(CacheObject.class, CacheChampionDto.class);
                CacheObject<CacheChampionDto> obj = objectMapper.readValue(jsonChampion, type);
                result.add(obj.getValue());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    @Override
    public List<CacheChampionDto> getTeamChampion(UUID riotUserSubId) {
        String key = combine(RedisKeys.TEAM_CHAMPION_KEY, riotUserSubId);
        List<String> champions = redisTemplate.opsForList().range(key, 0, -1);

        List<CacheChampionDto> result = new ArrayList<>();

        for (String jsonChampion : champions) {
            try {
                JavaType type = objectMapper.getTypeFactory().constructParametricType(CacheObject.class, CacheChampionDto.class);
                CacheObject<CacheChampionDto> obj = objectMapper.readValue(jsonChampion, type);
                result.add(obj.getValue());
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
}
