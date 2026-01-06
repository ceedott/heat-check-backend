package com.hockley.nba_stats.repositories;

import com.hockley.nba_stats.domain.entities.GameLog;
import com.hockley.nba_stats.domain.entities.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByFirstNameIgnoreCaseAndLastNameIgnoreCase(String first, String last);
}
