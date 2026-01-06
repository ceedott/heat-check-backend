package com.hockley.nba_stats.repository;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.domain.entities.Player;
import com.hockley.nba_stats.exceptions.PlayerGameStatNotFoundException;
import com.hockley.nba_stats.repositories.GameLogRepository;
import com.hockley.nba_stats.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PlayerRepositoryTest {
    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void testGetPlayerIdByFirstNameIgnoreCaseAndLastNameIgnoreCase() {
        Player player = playerRepository
                .findByFirstNameIgnoreCaseAndLastNameIgnoreCase("Precious", "Achiuwa")
                .orElseThrow(() -> new PlayerGameStatNotFoundException("Precious", "Achiuwa"));

        assertEquals(1630173, player.getPlayerId());

        System.out.println(player.toString());

    }

}
