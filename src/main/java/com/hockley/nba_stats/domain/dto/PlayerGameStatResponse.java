package com.hockley.nba_stats.domain.dto;

import java.time.LocalDate;

public record PlayerGameStatResponse(
        Long id,
        String fName,
        String lName,
        String team,
        LocalDate gameDate,
        Integer pts,
        Integer ast,
        Integer reb
        // more to be returned in future. good for now
) {
}
