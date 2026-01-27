package com.hockley.nba_stats.domain.dto;

import java.time.LocalDate;

public record GameLogResponse(
        Long id,
        String name, // in another table, might not even need player's name
        LocalDate gameDate,
        String team, // in another table
        String oppTeam, // in another table
        char winLoss,
        Integer minutes,
        Integer pts,
        Integer reb,
        Integer oreb,
        Integer dreb,
        Integer ast,
        Integer fgm,
        Integer fga,
        Double fgPer,
        Integer fg3m,
        Integer fg3a,
        Double fg3Per,
        Integer ftm,
        Integer fta,
        Double ftPer,
        Integer stl,
        Integer blk,
        Integer blka,
        Integer tov,
        Integer pf,
        Integer pfd,
        Integer plusMinus
) {
}
