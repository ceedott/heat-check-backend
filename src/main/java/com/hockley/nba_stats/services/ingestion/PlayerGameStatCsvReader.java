package com.hockley.nba_stats.services.ingestion;

import com.hockley.nba_stats.controllers.PlayerGameStatController;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class PlayerGameStatCsvReader {

    /**
     * Reads in csv, returns the list representation
     *
     * @param inputStream
     * @return rows, the rows of the csv column as row dto
     * @throws IOException
     */
    public List<PlayerGameStatCsvRow> read(InputStream inputStream) throws IOException{
        //requires header row in csv
        CSVFormat format = CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true) // dont treat header as data row
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        // this is a try with resources, semicolon used to separate resources
        // java automatically closes resources
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
             CSVParser parser = new CSVParser(br, format)) {

            List<PlayerGameStatCsvRow> rows = new ArrayList<>();

            for (CSVRecord r : parser) {
                rows.add(new PlayerGameStatCsvRow(
                        r.get("f_name"),
                        r.get("l_name"),
                        r.get("team"),
                        r.get("game_date"),
                        r.get("minutes"),
                        r.get("pts"),
                        r.get("reb"),
                        r.get("ast"),
                        r.get("fg"),
                        r.get("fga"),
                        r.get("three_p"),
                        r.get("three_pa"),
                        r.get("ft"),
                        r.get("fta"),
                        r.get("steals"),
                        r.get("blocks"),
                        r.get("turnovers"),
                        r.get("pf")
                ));
            }
            return rows;
        }
    }

}
