package com.hockley.nba_stats.domain.dto;

import java.time.LocalDate;

public record PlayerAveragesResponse(
        String fName,
        String lName,
        String team,
        Double minutes, // use double to handle potential null
        Double pts,
        Double reb,
        Double ast,
        Double fg,
        Double fga,
        Double fgPer,
        Double threeP,
        Double threePa,
        Double threePer,
        Double ft,
        Double fta,
        Double ftPer,
        Double steals,
        Double blocks,
        Double turnovers,
        Double pf
) {
}
