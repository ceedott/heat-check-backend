package com.hockley.nba_stats.mappers;

import com.hockley.nba_stats.domain.dto.PlayerGameStatRequest;
import com.hockley.nba_stats.domain.dto.PlayerGameStatResponse;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;

public interface PlayerGameStatMapper {
    PlayerGameStat fromDto(PlayerGameStatRequest playerGameStatRequest);
    PlayerGameStatResponse toDto(PlayerGameStat playerGameStat);
}
