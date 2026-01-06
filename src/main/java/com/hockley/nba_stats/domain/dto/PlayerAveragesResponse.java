package com.hockley.nba_stats.domain.dto;

import java.time.LocalDate;

public record PlayerAveragesResponse(
        Double minutes,
        Double pts,
        Double reb,
        Double oreb,
        Double dreb,
        Double ast,
        Double fgm,
        Double fga,
        Double fgPer,
        Double fg3m,
        Double fg3a,
        Double fg3Per,
        Double ftm,
        Double fta,
        Double ftPer,
        Double stl,
        Double blk,
        Double blka,
        Double tov,
        Double pf,
        Double pfD,
        Double plusMinus
) {
}
