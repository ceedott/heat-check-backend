package com.hockley.nba_stats.repositories;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlayerGameStatRepository extends JpaRepository<PlayerGameStat, Long> {
    Page<PlayerGameStat> findAll(Pageable pageable);
    Page<PlayerGameStat> findByFNameIgnoreCaseAndLNameIgnoreCase(String fName, String lName, Pageable pageable);
    boolean existsByFNameIgnoreCaseAndLNameIgnoreCaseAndGameDateAndTeam(
            String fName,
            String lName,
            LocalDate gameDate,
            String team
    );
    @Query(value = """
        SELECT
            f_name        AS fName,
            l_name        AS lName,
            team     AS team,

            AVG(minutes)::double precision AS minutes,
            AVG(pts)::double precision     AS pts,
            AVG(reb)::double precision     AS reb,
            AVG(ast)::double precision     AS ast,

            AVG(fg)::double precision      AS fg,
            AVG(fga)::double precision     AS fga,
            AVG(fg * 1.0 / NULLIF(fga, 0))::double precision AS fgPer,

            AVG(three_p)::double precision  AS threeP,
            AVG(three_pa)::double precision AS threePa,
            AVG(three_p * 1.0 / NULLIF(three_pa, 0))::double precision AS threePer,

            AVG(ft)::double precision      AS ft,
            AVG(fta)::double precision     AS fta,
            AVG(ft * 1.0 / NULLIF(fta, 0))::double precision AS ftPer,

            AVG(steals)::double precision   AS steals,
            AVG(blocks)::double precision   AS blocks,
            AVG(turnovers)::double precision AS turnovers,
            AVG(pf)::double precision       AS pf
        FROM player_game_stats
        WHERE LOWER(f_name) = LOWER(:first)
          AND LOWER(l_name) = LOWER(:last)
        GROUP BY f_name, l_name, team;
        """,
            nativeQuery = true)
    List<PlayerAveragesResponse> getPlayerAverages(@Param("first") String first, @Param("last") String last);
    // return a List not Optional List. Optional is for values not containers.
    // if player doesnt exist then u just return empty list
    // used native sql bc we couldnt use null with jpql

    List<PlayerGameStat> findByFNameIgnoreCaseAndLNameIgnoreCaseAndGameDateBetweenOrderByGameDate(String fName, String lName, LocalDate from, LocalDate to);

    @Query("""
    SELECT p
    FROM PlayerGameStat p
    WHERE LOWER(p.fName) = LOWER(:first)
      AND LOWER(p.lName) = LOWER(:last)
    ORDER BY p.gameDate DESC
""")
    List<PlayerGameStat> findRecentGames(
            @Param("first") String first,
            @Param("last") String last,
            Pageable pageable // acts as LIMIT command
    );
}

