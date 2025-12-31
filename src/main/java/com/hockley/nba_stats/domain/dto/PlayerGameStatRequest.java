package com.hockley.nba_stats.domain.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PlayerGameStatRequest(
        @NotBlank
        String fName,
        @NotBlank
        String lName,
        @NotBlank
        String team,
        @NotNull
        LocalDate gameDate,
        @Min(0)
        Integer minutes,
        @Min(0)
        Integer pts,
        @Min(0)
        Integer reb,
        @Min(0)
        Integer ast,
        @Min(0)
        Integer fg,
        @Min(0)
        Integer fga,
        @Min(0)
        Integer threeP,
        @Min(0)
        Integer threePa,
        @Min(0)
        Integer ft,
        @Min(0)
        Integer fta,
        @Min(0)
        Integer stl,
        @Min(0)
        Integer blk,
        @Min(0)
        Integer tov,
        @Min(0)
        Integer pf
) {
}
