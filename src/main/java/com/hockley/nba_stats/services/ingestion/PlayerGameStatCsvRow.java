package com.hockley.nba_stats.services.ingestion;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlayerGameStatCsvRow(
        // all fields should be string
        String fName,
        String lName,
        String team,
        String gameDate,
        String minutes,
        String pts,
        String reb,
        String ast,
        String fg,
        String fga,
        String threeP,
        String threePa,
        String ft,
        String fta,
        String stl,
        String blk,
        String tov,
        String pf
) {
}
