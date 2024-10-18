package com.gamelink.backend.global.config.redis;

public class RedisKeys {
    public static final String KEY_DELIMITER = ":";

    public static final String MATCH_ID_KEY = "match_id";
    public static final String SOLO_RANK_MATCH_ID_KEY = "solo_rank_match_id";
    public static final String TEAM_RANK_MATCH_ID_KEY = "team_rank_match_id";

    public static final String TOTAL_KDA_KEY = "kda_id";
    public static final String SOLO_KDA_KEY = "solo_kda_id";
    public static final String TEAM_KDA_KEY = "team_kda_id";

    public static final String TOTAL_CHAMPION_KEY = "champion_id";
    public static final String SOLO_CHAMPION_KEY = "solo_champion_id";
    public static final String TEAM_CHAMPION_KEY = "team_champion_id";

    public static String combine(Object key1, Object key2) {
        return key1 + KEY_DELIMITER + key2;
    }

    public static String combine(Object key1, Object key2, Object key3) {
        return key1 + KEY_DELIMITER + key2 + KEY_DELIMITER + key3;
    }

    public static String combine(Object... keys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            sb.append(keys[i]);
            if (i < keys.length - 1) {
                sb.append(KEY_DELIMITER);
            }
        }
        return sb.toString();
    }
}
