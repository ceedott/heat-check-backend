package com.hockley.nba_stats.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "player_game_stats",
        indexes = {
                @Index(name = "idx_player_date", columnList = "f_name,l_name,game_date"),
                @Index(name = "idx_team_date", columnList = "team,game_date")
        }
)
public class PlayerGameStat { // to be refactored
    // GAME LOG ENTITY
    // each game log should have
    // id, player name, minutes, pts, reb, ast, fg, fga, fg_per, 3p, 3pa, 3p_per, ft, fta, ft_per, stl, blk, tov, pf

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto incremented
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "f_name", nullable = false)
    private String fName;
    @Column(name = "l_name", nullable = false)
    private String lName;
    @Column(name = "team", nullable = false)
    private String team;
    @Column(name = "game_date", nullable = false)
    private LocalDate gameDate;
    @Column(name = "minutes", nullable = false)
    private int minutes;
    @Column(name = "pts", nullable = false)
    private int pts;
    @Column(name = "reb", nullable = false)
    private int reb;
    @Column(name = "ast", nullable = false)
    private int ast;
    @Column(name = "fg", nullable = false)
    private int fg;
    @Column(name = "fga", nullable = false)
    private int fga;
    @Column(name = "three_p", nullable = false)
    private int threeP;
    @Column(name = "three_pa", nullable = false)
    private int threePa;
    @Column(name = "ft", nullable = false)
    private int ft;
    @Column(name = "fta", nullable = false)
    private int fta;
    @Column(name = "steals", nullable = false)
    private int steals;
    @Column(name = "blocks", nullable = false)
    private int blocks;
    @Column(name = "turnovers", nullable = false)
    private int turnovers;
    @Column(name = "pf", nullable = false)
    private int pf;

    public PlayerGameStat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getPts() {
        return pts;
    }

    public void setPts(int pts) {
        this.pts = pts;
    }

    public int getReb() {
        return reb;
    }

    public void setReb(int reb) {
        this.reb = reb;
    }

    public int getAst() {
        return ast;
    }

    public void setAst(int ast) {
        this.ast = ast;
    }

    public int getFg() {
        return fg;
    }

    public void setFg(int fg) {
        this.fg = fg;
    }

    public int getFga() {
        return fga;
    }

    public void setFga(int fga) {
        this.fga = fga;
    }

    public int getThreeP() {
        return threeP;
    }

    public void setThreeP(int threeP) {
        this.threeP = threeP;
    }

    public int getThreePa() {
        return threePa;
    }

    public void setThreePa(int threePa) {
        this.threePa = threePa;
    }

    public int getFt() {
        return ft;
    }

    public void setFt(int ft) {
        this.ft = ft;
    }

    public int getFta() {
        return fta;
    }

    public void setFta(int fta) {
        this.fta = fta;
    }

    public int getSteals() {
        return steals;
    }

    public void setSteals(int steals) {
        this.steals = steals;
    }

    public int getBlocks() {
        return blocks;
    }

    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getTurnovers() {
        return turnovers;
    }

    public void setTurnovers(int turnovers) {
        this.turnovers = turnovers;
    }

    public int getPf() {
        return pf;
    }

    public void setPf(int pf) {
        this.pf = pf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerGameStat that = (PlayerGameStat) o;
        return id == that.id && minutes == that.minutes && pts == that.pts && reb == that.reb && ast == that.ast && fg == that.fg && fga == that.fga && threeP == that.threeP && threePa == that.threePa && ft == that.ft && fta == that.fta && steals == that.steals && blocks == that.blocks && turnovers == that.turnovers && pf == that.pf && Objects.equals(fName, that.fName) && Objects.equals(lName, that.lName) && Objects.equals(team, that.team) && Objects.equals(gameDate, that.gameDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fName, lName, team, gameDate, minutes, pts, reb, ast, fg, fga, threeP, threePa, ft, fta, steals, blocks, turnovers, pf);
    }
}
