package com.hockley.nba_stats.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "games"
)
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id", updatable = false, nullable = false)
    private Long gameId;
    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;
    @Column(name = "home_team_id")
    private Long homeTeamId;
    @Column(name = "away_team_id")
    private Long awayTeamId;

    public Game() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public Long getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(Long homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public Long getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(Long awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return Objects.equals(gameId, game.gameId) && Objects.equals(gameDate, game.gameDate) && Objects.equals(homeTeamId, game.homeTeamId) && Objects.equals(awayTeamId, game.awayTeamId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, gameDate, homeTeamId, awayTeamId);
    }
}
