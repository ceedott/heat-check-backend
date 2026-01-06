package com.hockley.nba_stats.repository;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.repositories.GameLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GameLogRepositoryTest {
    @Autowired
    private GameLogRepository gameLogRepository;

    @Test
    void testFindByPlayerId() {
        Long playerId = 1630173L;

        Pageable pageable = PageRequest.of(
                0,
                5,
                Sort.by("gameDate").descending()
        );

        var results = gameLogRepository.findByPlayerId(playerId, pageable);

        assertFalse(results.isEmpty());

        results.forEach(log ->
                System.out.println(
                        "Game " + log.getGameId() +
                                " | PTS: " + log.getPts()
                )
        );
    }

    @Test
    void testGetPlayerAverages() {
        Long playerId = 1630173L;

        List<PlayerAveragesResponse> averages = gameLogRepository.getPlayerAverages(playerId);

        assertFalse(averages.isEmpty());

        for (PlayerAveragesResponse response : averages) {
            System.out.println(response.toString());
        }

    }

    @Test
    void testfindByPlayerIdAndGameDateBetweenOrderByGameDate() {
        Long playerId = 1630173L; // precious achiuwa
        LocalDate from = LocalDate.of(2025, 12, 20);
        LocalDate to = LocalDate.of(2025, 12, 31);;
        List<GameLog> gameLogs = gameLogRepository.findByPlayerIdAndGameDateBetweenOrderByGameDate(playerId, from, to);

        assertFalse(gameLogs.isEmpty());

        for (GameLog gameLog : gameLogs) {
            System.out.println(gameLog.toString());
        }

    }

    @Test
    void testFindPlayerRecentGames() {
        Long playerId = 1630173L; // precious achiuwa

        Pageable pageable = PageRequest.of( // used as LIMIT
                0,
                5
        );

        List<GameLog> gameLogs = gameLogRepository.findPlayerRecentGames(playerId, pageable);

        assertFalse(gameLogs.isEmpty());

        for (GameLog gameLog : gameLogs) {
            System.out.println(gameLog.toString());
        }

    }

}
