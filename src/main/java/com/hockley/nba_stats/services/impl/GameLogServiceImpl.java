package com.hockley.nba_stats.services.impl;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.domain.entities.Player;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.domain.entities.StatType;
import com.hockley.nba_stats.exceptions.PlayerGameStatNotFoundException;
import com.hockley.nba_stats.repositories.GameLogRepository;
import com.hockley.nba_stats.repositories.PlayerGameStatRepository;
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
        return gameLogRepository.findById(gameLogId).orElseThrow(() -> new PlayerGameStatNotFoundException(gameLogId));
    }

    @Override
    public Page<GameLog> getGameLogsByGameId(Long gameId) {
        Pageable pageable = PageRequest.of(
                0,
                20
        );
        return gameLogRepository.findByGameId(gameId, pageable);
    }

    @Override
    public Page<GameLog> getTopByStat(StatType statType, Pageable pageable) {
        Pageable sortedPageTable = switch (statType) {
            // create new implementation of pageable, cannot modify old one bc pageable is immutable
            case PTS -> PageRequest.of(
                    pageable.getPageNumber(), // get page number client requested, if they requested a specific page
                    pageable.getPageSize(), // however many results the user asked for, keep that
                    Sort.by("pts").descending() // ignore any sort user sent, we want to sort by pts DESC
            );
            case REB -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("reb").descending()
            );
            case AST -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("ast").descending()
            );
        };

        // returns a page. contains list of results, total # rows, total pages, page num, is first/last page
        return gameLogRepository.findAll(sortedPageTable);
    }

    @Override
    @Transactional
    public Page<GameLog> getByfNameAndlName(String first, String last, Pageable pageable) {
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerGameStatNotFoundException(first, last));

        return gameLogRepository.findByPlayerId(player.getPlayerId(), pageable);
    }

    @Override
    public List<PlayerAveragesResponse> getPlayerAverages(String first, String last) {
        // its okay to return a response dto here because this is a query/data dto not a transport dto
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerGameStatNotFoundException(first, last));

        List<PlayerAveragesResponse> results = gameLogRepository.getPlayerAverages(player.getPlayerId());

        // if list empty, player not found. they have no game logs
        if (results.isEmpty()) {
            throw new PlayerGameStatNotFoundException(first, last);
        }

        return results;
    }

    @Override
    public List<RollingAverage> getPlayerRollingAverageByDate(String first, String last, LocalDate from, LocalDate to, StatType statType, int window) {
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase(first, last)
                .orElseThrow(() -> new PlayerGameStatNotFoundException(first, last));

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
                .orElseThrow(() -> new PlayerGameStatNotFoundException(first, last));

        List<GameLog> games = gameLogRepository.findPlayerRecentGames(player.getPlayerId(), limit);

        if (games.isEmpty()) {
            return List.of(); // no games, return empty list
        }

        // reverse so we process oldest to newest
        Collections.reverse(games);

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
