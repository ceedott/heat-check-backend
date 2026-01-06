package com.hockley.nba_stats.repositories;

import com.hockley.nba_stats.domain.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
