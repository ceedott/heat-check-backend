package com.hockley.nba_stats.services;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.domain.entities.HeatLevel;
import com.hockley.nba_stats.domain.entities.StatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface GameLogService {
    Page<GameLog> listPlayerGameStats();
    GameLog getGameLog(Long gameLogId);
    Page<GameLog> getGameLogsByGameId(Long gameId);
    Page<GameLog> getTopByStat(StatType statType, Pageable pageable);
    Page<GameLog> getByfNameAndlName(String fName, String lName, Pageable pageable);
    PlayerAveragesResponse getPlayerAverages(String first, String last);
    PlayerAveragesResponse getPlayerAveragesLastNGames(String first, String last, int n);
    List<RollingAverage> getPlayerRollingAverageByDate(String first, String last, LocalDate from, LocalDate to, StatType statType, int window);
    List<RollingAverage> getPlayerRollingAverageLastNGames(String first, String last, StatType statType, int games, int window);
    HeatLevel performHeatCheck(long playerId);
    Long getPlayerIdFromFirstAndLast(String first, String last);
}
