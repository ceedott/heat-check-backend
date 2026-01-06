package com.hockley.nba_stats.domain.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(
        name = "game_logs"
)
public class GameLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;
    @Column(name = "player_id", updatable = false)
    private Long playerId;
    @Column(name = "game_id", updatable = false)
    private Long gameId;
    @Column(name = "team_id", updatable = false)
    private Long teamId;
    @Column(name = "opponent_team_id", updatable = false)
    private Long opponentTeamId;
    @Column(name = "game_date", updatable = false)
    private LocalDate gameDate;
    @Column(name = "is_home", updatable = false)
    private Boolean isHome;
    @Column(name = "win_loss", updatable = false)
    private char winLoss;
    @Column(name = "minutes", updatable = false)
    private Integer minutes;
    @Column(name = "fgm", updatable = false)
    private Integer fgm;
    @Column(name = "fga", updatable = false)
    private Integer fga;
    @Column(name = "fg3m", updatable = false)
    private Integer fg3m;
    @Column(name = "fg3a", updatable = false)
    private Integer fg3a;
    @Column(name = "ftm", updatable = false)
    private Integer ftm;
    @Column(name = "fta", updatable = false)
    private Integer fta;
    @Column(name = "oreb", updatable = false)
    private Integer oreb;
    @Column(name = "dreb", updatable = false)
    private Integer dreb;
    @Column(name = "reb", updatable = false)
    private Integer reb;
    @Column(name = "ast", updatable = false)
    private Integer ast;
    @Column(name = "tov", updatable = false)
    private Integer tov;
    @Column(name = "stl", updatable = false)
    private Integer stl;
    @Column(name = "blk", updatable = false)
    private Integer blk;
    @Column(name = "blka", updatable = false)
    private Integer blka;
    @Column(name = "pf", updatable = false)
    private Integer pf;
    @Column(name = "pfd", updatable = false)
    private Integer pfd;
    @Column(name = "pts", updatable = false)
    private Integer pts;
    @Column(name = "plus_minus", updatable = false)
    private Integer plusMinus;

    public GameLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getOpponentTeamId() {
        return opponentTeamId;
    }

    public void setOpponentTeamId(Long opponentTeamId) {
        this.opponentTeamId = opponentTeamId;
    }

    public LocalDate getGameDate() {
        return gameDate;
    }

    public void setGameDate(LocalDate gameDate) {
        this.gameDate = gameDate;
    }

    public Boolean getHome() {
        return isHome;
    }

    public void setHome(Boolean home) {
        isHome = home;
    }

    public char getWinLoss() {
        return winLoss;
    }

    public void setWinLoss(char winLoss) {
        this.winLoss = winLoss;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getFgm() {
        return fgm;
    }

    public void setFgm(Integer fgm) {
        this.fgm = fgm;
    }

    public Integer getFga() {
        return fga;
    }

    public void setFga(Integer fga) {
        this.fga = fga;
    }

    public Integer getFg3m() {
        return fg3m;
    }

    public void setFg3m(Integer fg3m) {
        this.fg3m = fg3m;
    }

    public Integer getFg3a() {
        return fg3a;
    }

    public void setFg3a(Integer fg3a) {
        this.fg3a = fg3a;
    }

    public Integer getFtm() {
        return ftm;
    }

    public void setFtm(Integer ftm) {
        this.ftm = ftm;
    }

    public Integer getFta() {
        return fta;
    }

    public void setFta(Integer fta) {
        this.fta = fta;
    }

    public Integer getOreb() {
        return oreb;
    }

    public void setOreb(Integer oreb) {
        this.oreb = oreb;
    }

    public Integer getDreb() {
        return dreb;
    }

    public void setDreb(Integer dreb) {
        this.dreb = dreb;
    }

    public Integer getReb() {
        return reb;
    }

    public void setReb(Integer reb) {
        this.reb = reb;
    }

    public Integer getAst() {
        return ast;
    }

    public void setAst(Integer ast) {
        this.ast = ast;
    }

    public Integer getTov() {
        return tov;
    }

    public void setTov(Integer tov) {
        this.tov = tov;
    }

    public Integer getStl() {
        return stl;
    }

    public void setStl(Integer stl) {
        this.stl = stl;
    }

    public Integer getBlk() {
        return blk;
    }

    public void setBlk(Integer blk) {
        this.blk = blk;
    }

    public Integer getBlka() {
        return blka;
    }

    public void setBlka(Integer blka) {
        this.blka = blka;
    }

    public Integer getPf() {
        return pf;
    }

    public void setPf(Integer pf) {
        this.pf = pf;
    }

    public Integer getPfd() {
        return pfd;
    }

    public void setPfd(Integer pfd) {
        this.pfd = pfd;
    }

    public Integer getPts() {
        return pts;
    }

    public void setPts(Integer pts) {
        this.pts = pts;
    }

    public Integer getPlusMinus() {
        return plusMinus;
    }

    public void setPlusMinus(Integer plusMinus) {
        this.plusMinus = plusMinus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameLog gameLog = (GameLog) o;
        return winLoss == gameLog.winLoss && Objects.equals(id, gameLog.id) && Objects.equals(playerId, gameLog.playerId) && Objects.equals(gameId, gameLog.gameId) && Objects.equals(teamId, gameLog.teamId) && Objects.equals(opponentTeamId, gameLog.opponentTeamId) && Objects.equals(gameDate, gameLog.gameDate) && Objects.equals(isHome, gameLog.isHome) && Objects.equals(minutes, gameLog.minutes) && Objects.equals(fgm, gameLog.fgm) && Objects.equals(fga, gameLog.fga) && Objects.equals(fg3m, gameLog.fg3m) && Objects.equals(fg3a, gameLog.fg3a) && Objects.equals(ftm, gameLog.ftm) && Objects.equals(fta, gameLog.fta) && Objects.equals(oreb, gameLog.oreb) && Objects.equals(dreb, gameLog.dreb) && Objects.equals(reb, gameLog.reb) && Objects.equals(ast, gameLog.ast) && Objects.equals(tov, gameLog.tov) && Objects.equals(stl, gameLog.stl) && Objects.equals(blk, gameLog.blk) && Objects.equals(blka, gameLog.blka) && Objects.equals(pf, gameLog.pf) && Objects.equals(pfd, gameLog.pfd) && Objects.equals(pts, gameLog.pts) && Objects.equals(plusMinus, gameLog.plusMinus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playerId, gameId, teamId, opponentTeamId, gameDate, isHome, winLoss, minutes, fgm, fga, fg3m, fg3a, ftm, fta, oreb, dreb, reb, ast, tov, stl, blk, blka, pf, pfd, pts, plusMinus);
    }

    @Override
    public String toString() {
        return "GameLog{" +
                "id=" + id +
                ", playerId=" + playerId +
                ", gameId=" + gameId +
                ", teamId=" + teamId +
                ", opponentTeamId=" + opponentTeamId +
                ", gameDate=" + gameDate +
                ", isHome=" + isHome +
                ", winLoss=" + winLoss +
                ", minutes=" + minutes +
                ", fgm=" + fgm +
                ", fga=" + fga +
                ", fg3m=" + fg3m +
                ", fg3a=" + fg3a +
                ", ftm=" + ftm +
                ", fta=" + fta +
                ", oreb=" + oreb +
                ", dreb=" + dreb +
                ", reb=" + reb +
                ", ast=" + ast +
                ", tov=" + tov +
                ", stl=" + stl +
                ", blk=" + blk +
                ", blka=" + blka +
                ", pf=" + pf +
                ", pfd=" + pfd +
                ", pts=" + pts +
                ", plusMinus=" + plusMinus +
                '}';
    }
}
