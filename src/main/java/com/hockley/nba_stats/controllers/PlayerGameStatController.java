package com.hockley.nba_stats.controllers;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.PlayerGameStatRequest;
import com.hockley.nba_stats.domain.dto.PlayerGameStatResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.domain.entities.StatType;
import com.hockley.nba_stats.mappers.PlayerGameStatMapper;
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

@RestController()
@RequestMapping("/stats2")
public class PlayerGameStatController {
    private final PlayerGameStatMapper mapper;
    private final PlayerGameStatService service;
    private final PlayerGameStatCsvIngestionService csvIngestionService;

    public PlayerGameStatController(PlayerGameStatMapper mapper, PlayerGameStatService service, PlayerGameStatCsvIngestionService csvIngestionService) {
        this.mapper = mapper;
        this.service = service;
        this.csvIngestionService = csvIngestionService;
    }

    @GetMapping
    public List<PlayerGameStatResponse> listPlayerGameStats(){
        return service.listPlayerGameStats().stream().map(mapper::toDto).toList();
    }

    @PostMapping
    public ResponseEntity<PlayerGameStatResponse> createPlayerGameStat(@Valid @RequestBody PlayerGameStatRequest request) {
        PlayerGameStat playerGameStat = service.createPlayerGameStat(mapper.fromDto(request));
        // return a ResponseEntity instead of just response bc
        // it lets us control the response code instead of defaulting to 200
        // POST should return 201 if resource is successfully created
        return ResponseEntity // TODO SEE IF THERES A MORE OPTIMAL WAY TO RETURN
                .status(HttpStatus.CREATED) // check if actually created
                .body(mapper.toDto(playerGameStat));
    }

    @GetMapping("/{player_game_stat_id}")
    public ResponseEntity<PlayerGameStatResponse> getPlayerGameStat(
            @PathVariable("player_game_stat_id") Long playerGameStatId
    ) {
        // Optional belongs in service layer, not here. unwrap any optional
        return ResponseEntity.ok(
                mapper.toDto(service.getPlayerGameStat(playerGameStatId))
        );
    }

    @PutMapping("/{player_game_stat_id}")
    public ResponseEntity<PlayerGameStatResponse> updatePlayerGameStat(
            @PathVariable("player_game_stat_id") Long playerGameStatId,
            @Valid @RequestBody PlayerGameStatRequest request
    ) {
        PlayerGameStat updated = service.updatePlayerGameStat(playerGameStatId, mapper.fromDto(request));
        return ResponseEntity.ok(mapper.toDto(updated));
    }

    @DeleteMapping("/{player_game_stat_id}")
    public ResponseEntity<Void> deletePlayerGameStat(
            @PathVariable("player_game_stat_id") Long playerGameStatId
    ) {
        service.deletePlayerGameStat(playerGameStatId);
        return ResponseEntity.noContent().build(); // return 204 status code
    }

    @GetMapping("/top")
    public ResponseEntity<Page<PlayerGameStatResponse>> getTopByStat(
            @RequestParam StatType stat,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PlayerGameStatResponse> response = service.getTopByStat(stat, pageable).map(mapper::toDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PlayerGameStatResponse>> getByfNameAndlName(
            @RequestParam String first,
            @RequestParam String last,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<PlayerGameStatResponse> response = service.getByfNameAndlName(first, last, pageable).map(mapper::toDto);
        return ResponseEntity.ok(response);
    }

    // /stats/averages?first=Lebron&last=James
    @GetMapping("/averages")
    public ResponseEntity<List<PlayerAveragesResponse>> getPlayerAverages(
            @RequestParam String first,
            @RequestParam String last
    ) {
        return ResponseEntity.ok(service.getPlayerAverages(first, last));
    }

    @PostMapping("/ingestion")                      // requestparam for file. requestbody for json
    public ResponseEntity<IngestionResult> uploadCsv(@RequestParam("file")MultipartFile file) throws Exception{
        return ResponseEntity.ok(csvIngestionService.ingest(file));
    }

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

        List<RollingAverage> rollingAverages = service.getPlayerRollingAverageByDate(first, last, from, to, stat, window);
        return ResponseEntity.ok(rollingAverages);
    }

    @GetMapping("/trends/recent")
    public ResponseEntity<List<RollingAverage>> getPlayerRollingAverages(
            @RequestParam String first,
            @RequestParam String last,
            @RequestParam StatType stat,
            @RequestParam int games,
            @RequestParam(defaultValue = "5") int window
    ) {
        int windowValid = Math.max(1, Math.min(window, 10));
        int gamesValid = Math.max(1, Math.min(games, 15));

        List<RollingAverage> rollingAverages = service.getPlayerRollingAverage(first, last, stat, gamesValid, windowValid);
        return ResponseEntity.ok(rollingAverages);
    }

}
