package com.hockley.nba_stats.repositories;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.entities.GameLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameLogRepository extends JpaRepository<GameLog, Long> {

    Page<GameLog> findAll(Pageable pageable);
    Page<GameLog> findByGameId(Long gameId, Pageable pageable);
    Page<GameLog> findByPlayerId(Long playerId, Pageable pageable);
    @Query(value = """
        SELECT
            AVG(minutes)::double precision AS minutes,
            AVG(pts)::double precision     AS pts,
            AVG(reb)::double precision     AS reb,
            AVG(oreb)::double precision     AS oreb,
            AVG(dreb)::double precision     AS dreb,
            AVG(ast)::double precision     AS ast,

            AVG(fgm)::double precision      AS fgm,
            AVG(fga)::double precision     AS fga,
            AVG(fgm * 1.0 / NULLIF(fga, 0))::double precision AS fgPer,

            AVG(fg3m)::double precision  AS fg3m,
            AVG(fg3a)::double precision AS fg3a,
            AVG(fg3m * 1.0 / NULLIF(fg3a, 0))::double precision AS fg3Per,

            AVG(ftm)::double precision      AS ftm,
            AVG(fta)::double precision     AS fta,
            AVG(ftm * 1.0 / NULLIF(fta, 0))::double precision AS ftPer,

            AVG(stl)::double precision   AS stl,
            AVG(blk)::double precision   AS blk,
            AVG(blka)::double precision   AS blka,
            AVG(tov)::double precision AS tov,
            AVG(pf)::double precision       AS pf,
            AVG(pfd)::double precision       AS pfd,
            AVG(plus_minus)::double precision       AS plusMinus
        FROM game_logs
        WHERE player_id = :player_id
        """,
            nativeQuery = true)
    Optional<PlayerAveragesResponse> getPlayerAverages(@Param("player_id") long playerId);
    // return a List not Optional List. Optional is for values not containers.
    // if player doesnt exist then u just return empty list
    // used native sql bc we couldnt use null with jpql
    // ^ on second thought maybe this should be optional

    @Query(value = """
        SELECT
            AVG(minutes)::double precision AS minutes,
            AVG(pts)::double precision     AS pts,
            AVG(reb)::double precision     AS reb,
            AVG(oreb)::double precision     AS oreb,
            AVG(dreb)::double precision     AS dreb,
            AVG(ast)::double precision     AS ast,

            AVG(fgm)::double precision      AS fgm,
            AVG(fga)::double precision     AS fga,
            AVG(fgm * 1.0 / NULLIF(fga, 0))::double precision AS fgPer,

            AVG(fg3m)::double precision  AS fg3m,
            AVG(fg3a)::double precision AS fg3a,
            AVG(fg3m * 1.0 / NULLIF(fg3a, 0))::double precision AS fg3Per,

            AVG(ftm)::double precision      AS ftm,
            AVG(fta)::double precision     AS fta,
            AVG(ftm * 1.0 / NULLIF(fta, 0))::double precision AS ftPer,

            AVG(stl)::double precision   AS stl,
            AVG(blk)::double precision   AS blk,
            AVG(blka)::double precision   AS blka,
            AVG(tov)::double precision AS tov,
            AVG(pf)::double precision       AS pf,
            AVG(pfd)::double precision       AS pfd,
            AVG(plus_minus)::double precision       AS plusMinus
        FROM (
            SELECT *
            FROM game_logs
            WHERE player_id = :player_id
            ORDER BY game_date DESC
            LIMIT :n
        ) recent_games;
        """,
            nativeQuery = true)
    Optional<PlayerAveragesResponse> getPlayerAveragesLastNGames(@Param("player_id") long playerId, @Param("n") int n);

    List<GameLog> findByPlayerIdAndGameDateBetweenOrderByGameDate(Long playerId, LocalDate from, LocalDate to);

    @Query("""
    SELECT p
    FROM GameLog p
    WHERE p.playerId = :playerId
    ORDER BY p.gameDate DESC
""")
    List<GameLog> findPlayerRecentGames(
            @Param("playerId") Long playerId,
            Pageable pageable // acts as LIMIT command
    );

}
