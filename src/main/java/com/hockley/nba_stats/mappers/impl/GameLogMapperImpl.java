package com.hockley.nba_stats.mappers.impl;

import com.hockley.nba_stats.domain.dto.GameLogResponse;
import com.hockley.nba_stats.domain.entities.*;
import com.hockley.nba_stats.exceptions.PlayerNotFoundException;
import com.hockley.nba_stats.mappers.GameLogMapper;
import com.hockley.nba_stats.repositories.GameRepository;
import com.hockley.nba_stats.repositories.PlayerRepository;
import com.hockley.nba_stats.repositories.TeamRepository;
import org.springframework.stereotype.Component;

@Component
public class GameLogMapperImpl implements GameLogMapper {

    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final TeamRepository teamRepository;

    public GameLogMapperImpl(PlayerRepository playerRepository, GameRepository gameRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.teamRepository = teamRepository;
    }

    @Override
    public GameLogResponse toDto(GameLog gameLog) {

        Player player = playerRepository
                .findById(gameLog.getPlayerId())
                .orElseThrow(() -> new PlayerNotFoundException(gameLog.getPlayerId()));

        Team team = teamRepository.findById(gameLog.getTeamId()).orElseThrow(); //FIXME
        Team oppTeam = teamRepository.findById(gameLog.getOpponentTeamId()).orElseThrow(); //FIXME

        return new GameLogResponse(
                player.getFullName(),
                gameLog.getGameDate(),
                team.getAbbrev(),
                oppTeam.getAbbrev(),
                gameLog.getWinLoss(),
                gameLog.getMinutes(),
                gameLog.getPts(),
                gameLog.getReb(),
                gameLog.getOreb(),
                gameLog.getDreb(),
                gameLog.getAst(),
                gameLog.getFgm(),
                gameLog.getFga(),
                (gameLog.getFga() > 0) ? (gameLog.getFgm() / (double) gameLog.getFga()) * 100 : 0,
                gameLog.getFg3m(),
                gameLog.getFg3a(),
                (gameLog.getFg3a() > 0) ? (gameLog.getFg3m() / (double) gameLog.getFg3a()) * 100 : 0,
                gameLog.getFtm(),
                gameLog.getFta(),
                (gameLog.getFtm() > 0) ? (gameLog.getFtm() / (double) gameLog.getFta()) * 100 : 0,
                gameLog.getStl(),
                gameLog.getBlk(),
                gameLog.getBlka(),
                gameLog.getTov(),
                gameLog.getPf(),
                gameLog.getPfd(),
                gameLog.getPlusMinus()
        );
    }
}
