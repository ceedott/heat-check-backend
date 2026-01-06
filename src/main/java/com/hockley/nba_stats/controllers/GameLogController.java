package com.hockley.nba_stats.controllers;

import com.hockley.nba_stats.domain.dto.*;
import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.domain.entities.StatType;
import com.hockley.nba_stats.mappers.GameLogMapper;
import com.hockley.nba_stats.mappers.PlayerGameStatMapper;
import com.hockley.nba_stats.services.GameLogService;
import com.hockley.nba_stats.services.PlayerGameStatService;
import com.hockley.nba_stats.services.ingestion.IngestionResult;
import com.hockley.nba_stats.services.ingestion.PlayerGameStatCsvIngestionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/stats")
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

    @GetMapping("/games/{game_id}")
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
            @RequestParam String first,
            @RequestParam String last,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<GameLogResponse> response = gameLogService.getByfNameAndlName(first, last, pageable).map(gameLogMapper::toDto);
        return ResponseEntity.ok(response);
    }

    // /stats/averages?first=Lebron&last=James
    @GetMapping("/averages")
    public ResponseEntity<List<PlayerAveragesResponse>> getPlayerAverages(
            @RequestParam String first,
            @RequestParam String last
    ) {
        return ResponseEntity.ok(gameLogService.getPlayerAverages(first, last));
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
    @GetMapping("/trends/recent")
    public ResponseEntity<List<RollingAverage>> getPlayerRollingAveragesLastNGames(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam StatType stat,
            @RequestParam int games,
            @RequestParam(defaultValue = "5") int window
    ) {
        int windowValid = Math.max(1, Math.min(window, 10));
        int gamesValid = Math.max(1, Math.min(games, 15));

        List<RollingAverage> rollingAverages = gameLogService.getPlayerRollingAverageLastNGames(first, last, stat, gamesValid, windowValid);
        return ResponseEntity.ok(rollingAverages);
    }
}
