package com.hockley.nba_stats.domain.dto;

import java.time.LocalDate;

public record RollingAverage(
        LocalDate gameDate,
        Integer value,
        Double avg
) {
}
