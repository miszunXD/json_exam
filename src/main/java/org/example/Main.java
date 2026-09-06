package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.model.RobotReport;

import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1.1 Do ukrycia pola podczas serializacji - @JsonIgnore
        // 1.2 Do zmiany nazwy pola w Json należy użyć @JsonProperty

        //1.2 Dla pliku bajtowego firmware_update.bin należy użyc strumieni bajtowych InputStream/OutputStream
        // Dla pliku daily_logs.txt używamy strumieni znakowych Reader/Writer


        // Cz.2
        String json = """
                {
                  "task_id": "TSK-882",
                  "urgent": true,
                  "payload": {
                    "items": [
                      {"id": 101, "weight": 2.5},
                      {"id": 102, "weight": 4.1}
                    ],
                    "destination": "Sektor A"
                  }
                }
                """;

        //2.1
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);

        //2.2
        if (node.path("urgent").asBoolean()) {
            System.out.println("Zlecenie TSK-882 jest pilne!");
        }

        //2.3
        JsonNode items = node.path("payload")
                .path("items");

        double weight = 0;

        if (items.isArray()) {
            for (JsonNode item : items) {
                weight += item.path("weight").asDouble();
            }
        }

        System.out.println("Łączna waga: " + weight);

        //3.2
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());

        //3.3
        String json1 = """
                [
                  {
                    "serialNumber": "RBT-01",
                    "commissionDate": "15-11-2025",
                    "battery": 88
                  },
                  {
                    "serialNumber": "RBT-02",
                    "commissionDate": "20-11-2025",
                    "battery": 45,
                    "needsMaintenance": true
                  }
                ]
                """;

        List<RobotReport> reports =  mapper.readValue(
                json1,
                new  TypeReference<List<RobotReport>>(){});

        reports.forEach(report -> System.out.println(report.serialNumber() + " | " + report.commissionDate()));

    }
}