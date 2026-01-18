package com.hockley.nba_stats.controllers;

import com.hockley.nba_stats.domain.dto.*;
import com.hockley.nba_stats.domain.entities.*;
import com.hockley.nba_stats.mappers.GameLogMapper;
import com.hockley.nba_stats.services.GameLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/games")
public class GameLogController {
    private final GameLogMapper gameLogMapper;
    private final GameLogService gameLogService;

    public GameLogController(GameLogMapper gameLogMapper, GameLogService gameLogService) {
        this.gameLogMapper = gameLogMapper;
        this.gameLogService = gameLogService;
    }

    @GetMapping
    public ResponseEntity<Page<GameLogResponse>> listPlayerGameStats(){
        Page<GameLogResponse> gameLogResponses = gameLogService.listPlayerGameStats().map(gameLogMapper::toDto);
        return ResponseEntity
                .ok(gameLogResponses);
    }

    @GetMapping("/{game_log_id}")
    public ResponseEntity<GameLogResponse> getGameLog(
            @PathVariable("game_log_id") Long gameLogId
    ) {
        // Optional belongs in service layer, not here
        return ResponseEntity.ok(
                gameLogMapper.toDto(gameLogService.getGameLog(gameLogId))
        );
    }

    @GetMapping("/game/{game_id}")
    public ResponseEntity<Page<GameLogResponse>> getGameLogsByGameId(
            @PathVariable("game_id") Long gameId
    ) {
        return ResponseEntity.ok(
                gameLogService.getGameLogsByGameId(gameId).map(gameLogMapper::toDto)
        );
    }

    @GetMapping("/top")
    public ResponseEntity<Page<GameLogResponse>> getTopByStat(
            @RequestParam StatType stat,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<GameLogResponse> response = gameLogService.getTopByStat(stat, pageable).map(gameLogMapper::toDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<GameLogResponse>> getByfNameAndlName(
            @RequestParam String first, // first and last sent as query parameters in endpoint
            @RequestParam String last,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<GameLogResponse> response = gameLogService.getByfNameAndlName(first, last, pageable).map(gameLogMapper::toDto);
        return ResponseEntity.ok(response);
    }

    // /stats/averages?first=Lebron&last=James
    @GetMapping("/averages")
    public ResponseEntity<PlayerAveragesResponse> getPlayerAverages(
            @RequestParam String first, // query params
            @RequestParam String last
    ) {
        return ResponseEntity.ok(gameLogService.getPlayerAverages(first, last));
    }

    @GetMapping("/averages/recent")
    public ResponseEntity<PlayerAveragesResponse> getPlayerAveragesLastNGames(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam int n
    ) {
        return ResponseEntity.ok(gameLogService.getPlayerAveragesLastNGames(first, last, n));
    }

    // implement pagination?
    @GetMapping("/trends/range")
    public ResponseEntity<List<RollingAverage>> getPlayerRollingAveragesByDate(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to,
            @RequestParam StatType stat,
            @RequestParam(defaultValue = "5") int windowArg
    ) {
        int window = Math.max(1, Math.min(windowArg, 10));

        List<RollingAverage> rollingAverages = gameLogService.getPlayerRollingAverageByDate(first, last, from, to, stat, window);
        return ResponseEntity.ok(rollingAverages);
    }

    // implement pagination?
    // be wary of rolling averages. the 1st game will only have sample size of 1.
    @GetMapping("/trends/recent")
    public ResponseEntity<List<RollingAverage>> getPlayerRollingAveragesLastNGames(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam StatType stat,
            @RequestParam int games,
            @RequestParam(defaultValue = "5") int window
    ) {
        int windowValid = Math.max(1, Math.min(window, 10)); // max window size is 10
        int gamesValid = Math.max(1, Math.min(games, 15)); // max # games is 15

        List<RollingAverage> rollingAverages = gameLogService.getPlayerRollingAverageLastNGames(first, last, stat, gamesValid, windowValid);
        return ResponseEntity.ok(rollingAverages);
    }

    @GetMapping("/search/matchups")
    public ResponseEntity<List<GameLogResponse>> getMatchupsVersusTeam() {
        return null;
    }

    @GetMapping("/search/matchups/averages")
    public ResponseEntity<PlayerAveragesResponse> getMatchupAveragesVersusTeam() {
        return null;
    }

    // not a client interactable endpoint so going to send player id instead of name as param
    @GetMapping("/heat_check")
    public ResponseEntity<HeatLevel> heatCheck(
            @RequestParam long playerId
    ) {
        // heat check based on last 5 games
        return ResponseEntity.ok(gameLogService.performHeatCheck(playerId));
    }

    //helper endpoint to find player id
    @GetMapping
    @RequestMapping("/find_player_id")
    public ResponseEntity<Long> getPlayerIdFromFirstAndLast(
            @RequestParam String first,
            @RequestParam String last
    ) {
        return ResponseEntity.ok(gameLogService.getPlayerIdFromFirstAndLast(first, last));
    }

    // get player's most recent game

    // see other team's past 5 games

    // get averages of similar players/position vs next matchup

}
