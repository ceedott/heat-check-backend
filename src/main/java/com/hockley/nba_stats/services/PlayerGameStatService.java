package com.hockley.nba_stats.services;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.domain.entities.StatType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PlayerGameStatService {
    List<PlayerGameStat> listPlayerGameStats();
    PlayerGameStat createPlayerGameStat(PlayerGameStat playerGameStat);
    PlayerGameStat getPlayerGameStat(Long playerGameStatId);
    PlayerGameStat updatePlayerGameStat(Long playerGameStatId, PlayerGameStat playerGameStat);
    void deletePlayerGameStat(Long playerGameStatId);
    Page<PlayerGameStat> getTopByStat(StatType statType, Pageable pageable);
    Page<PlayerGameStat> getByfNameAndlName(String fName, String lName, Pageable pageable);
    List<PlayerAveragesResponse> getPlayerAverages(String first, String last);
    List<RollingAverage> getPlayerRollingAverageByDate(String first, String last, LocalDate from, LocalDate to, StatType statType, int window);
    List<RollingAverage> getPlayerRollingAverage(String first, String last, StatType statType, int games, int window);

}
