package com.hockley.nba_stats.repository;

import com.hockley.nba_stats.domain.entities.Player;
import com.hockley.nba_stats.exceptions.PlayerNotFoundException;
import com.hockley.nba_stats.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

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
                .orElseThrow(() -> new PlayerNotFoundException("Precious", "Achiuwa"));

        assertEquals(1630173, player.getPlayerId());

        System.out.println(player.toString());

    }

}
