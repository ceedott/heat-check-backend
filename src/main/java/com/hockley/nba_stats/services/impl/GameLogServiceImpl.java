package com.hockley.nba_stats.services.impl;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.*;
import com.hockley.nba_stats.exceptions.GameLogNotFoundException;
import com.hockley.nba_stats.exceptions.PlayerNotFoundException;
import com.hockley.nba_stats.repositories.GameLogRepository;
import com.hockley.nba_stats.repositories.PlayerRepository;
import com.hockley.nba_stats.services.GameLogService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class GameLogServiceImpl implements GameLogService {
    private final GameLogRepository gameLogRepository;
    private final PlayerRepository playerRepository;

    public GameLogServiceImpl(GameLogRepository gameLogRepository, PlayerRepository playerRepository) {
        this.gameLogRepository = gameLogRepository;
        this.playerRepository = playerRepository;
    }

    @Override
    public Page<GameLog> listPlayerGameStats() {
        Pageable pageable = PageRequest.of(
                0,
                100
        );
        return gameLogRepository.findAll(pageable);
    }

    @Override
    public GameLog getGameLog(Long gameLogId) {
        return gameLogRepository.findById(gameLogId).orElseThrow(() -> new GameLogNotFoundException(gameLogId));
    }

    @Override
    public Page<GameLog> getGameLogsByGameId(Long gameId) {
        Pageable pageable = PageRequest.of(
                0,
                30
        );
        return gameLogRepository.findByGameId(gameId, pageable);
    }

    @Override
    public Page<GameLog> getTopByStat(StatType statType, Pageable pageable) {
        Pageable sortedPageTable = PageRequest.of( // removed switch case, this is much sorter
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(statType.getColumn()).descending()
        );

        // returns a page. contains list of results, total # rows, total pages, page num, is first/last page
        return gameLogRepository.findAll(sortedPageTable);
    }

    @Override
    @Transactional
    public Page<GameLog> getByfNameAndlName(String first, String last, Pageable pageable) {
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        return gameLogRepository.findByPlayerId(player.getPlayerId(), pageable);
    }

    @Override
    public PlayerAveragesResponse getPlayerAverages(String first, String last) {

        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        //System.out.println(player.getFullName());

        PlayerAveragesResponse results = gameLogRepository.getPlayerAverages(player.getPlayerId())
                .orElseThrow(() -> new GameLogNotFoundException(player.getPlayerId()));

        return results;
    }

    @Override
    public PlayerAveragesResponse getPlayerAveragesLastNGames(String first, String last, int n) {

        if (n <= 0) {
            throw new IllegalArgumentException("n must be > 0");
        }

        Player player = playerRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        return gameLogRepository
                .getPlayerAveragesLastNGames(player.getPlayerId(), n)
                .orElseThrow(() -> new GameLogNotFoundException(player.getPlayerId()));
    }

    @Override
    public List<RollingAverage> getPlayerRollingAverageByDate(String first, String last, LocalDate from, LocalDate to, StatType statType, int window) {
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        List<GameLog> games = gameLogRepository.findByPlayerIdAndGameDateBetweenOrderByGameDate(player.getPlayerId(), from, to);

        List<RollingAverage> rollingAverages = new ArrayList<>();
        Deque<Integer> q = new ArrayDeque<>();
        int runningSum = 0;

        for (GameLog game : games) {
            int stat = switch (statType) {
                case PTS -> game.getPts();
                case REB -> game.getReb();
                case AST -> game.getAst();
            };

            q.addLast(stat);
            runningSum += stat;
            if (q.size() > window) {
                runningSum -= q.removeFirst();
            }

            double avg = runningSum / (double)q.size();

            rollingAverages.add(new RollingAverage(
                    game.getGameDate(),
                    stat,
                    avg
            ));
        }

        return rollingAverages;

    }

    @Override
    public List<RollingAverage> getPlayerRollingAverageLastNGames(String first, String last, StatType statType, int gamesAmt, int window) {
        Pageable limit = PageRequest.of(0, gamesAmt); // page 0, gameAmt page size
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        List<GameLog> games = gameLogRepository
                .findPlayerRecentGames(player.getPlayerId(), limit);

        if (games.isEmpty()) {
            return List.of(); // no games, return empty list
        }

        // reverse so we process oldest to newest
        Collections.reverse(games);

        return computeRollingAverage(games, statType, window);

    }

    // calculate heat level of player by comparing season average to their recent average of last 5 games
    @Override
    public HeatLevel performHeatCheck(long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        PlayerAveragesResponse recent = getPlayerAveragesLastNGames(player.getFirstName(), player.getLastName(), 5);
        PlayerAveragesResponse season = getPlayerAverages(player.getFirstName(), player.getLastName());

        //TODO ONLY CALCULATES BASED ON POINTS RN
        return calculateHeatLevel(recent, season);
    }

    public Long getPlayerIdFromFirstAndLast(String first, String last) {
        Player player = playerRepository.findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerNotFoundException(first, last));

        return player.getPlayerId();
    }

    //helper function
    private HeatLevel calculateHeatLevel(PlayerAveragesResponse recent, PlayerAveragesResponse season) {
        // ONLY CALCULATE BASED ON POINTS RIGHT NOW

        Double recentPts = recent.pts();
        Double seasonPts = season.pts();

        if (seasonPts == 0) { // avoid dividing by 0
            return HeatLevel.NEUTRAL;
        }

        // calculate percent difference
        double percentChange = (recentPts - seasonPts) / seasonPts;

        if (percentChange >= 0.10) {
            return HeatLevel.HOT;
        } else if (percentChange <= -0.10) {
            return HeatLevel.COLD;
        } else {
            return HeatLevel.NEUTRAL;
        }
    }

    // helper function
    private List<RollingAverage> computeRollingAverage(List<GameLog> games, StatType statType, int window) {
        List<RollingAverage> rollingAverages = new ArrayList<>();
        Deque<Integer> q = new ArrayDeque<>();
        int runningSum = 0;

        for (GameLog game : games) {
            int stat = switch (statType) {
                case PTS -> game.getPts();
                case REB -> game.getReb();
                case AST -> game.getAst();
            };

            q.addLast(stat);
            runningSum += stat;
            if (q.size() > window) {
                runningSum -= q.removeFirst();
            }

            double avg = runningSum / (double)q.size();

            rollingAverages.add(new RollingAverage(
                    game.getGameDate(),
                    stat,
                    avg
            ));
        }

        return rollingAverages;
    }

}
