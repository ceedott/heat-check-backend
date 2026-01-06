package com.hockley.nba_stats.repositories;

import com.hockley.nba_stats.domain.entities.Game;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
}
