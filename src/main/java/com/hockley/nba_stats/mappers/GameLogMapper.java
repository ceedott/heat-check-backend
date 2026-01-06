package com.hockley.nba_stats.mappers;

import com.hockley.nba_stats.domain.dto.GameLogResponse;
import com.hockley.nba_stats.domain.entities.GameLog;

public interface GameLogMapper {
    GameLogResponse toDto(GameLog gameLog);

}
