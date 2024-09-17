package com.gamelink.backend.domain.user.util;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class NicknameProvider {

    private static final String CSV_FILE_PATH = "src/main/resources/nickname.csv";

    public String generateNickname() {
        List<String> adjectives = new ArrayList<>();
        List<String> championNames = new ArrayList<>();

        try {
            try (CSVReader reader = new CSVReader(new FileReader(CSV_FILE_PATH))) {
                String[] nextLine;
                reader.readNext();
                while ((nextLine = reader.readNext()) != null) {
                    adjectives.add(nextLine[0]);      // 첫 번째 열: 수식어
                    championNames.add(nextLine[1]);   // 두 번째 열: 롤 캐릭터 이름
                }
            } catch (CsvValidationException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Random random = new Random();
        String adjective = adjectives.get(random.nextInt(adjectives.size()));
        String championName = championNames.get(random.nextInt(championNames.size()));
        return adjective + " " + championName;
    }
}
