package com.hockley.nba_stats.mappers.impl;

import com.hockley.nba_stats.domain.dto.PlayerGameStatRequest;
import com.hockley.nba_stats.domain.dto.PlayerGameStatResponse;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.mappers.PlayerGameStatMapper;
import org.springframework.stereotype.Component;

@Component
public class PlayerGameStatMapperImpl implements PlayerGameStatMapper {
    @Override
    public PlayerGameStat fromDto(PlayerGameStatRequest request) {
        PlayerGameStat entity = new PlayerGameStat();
        // use setters instead of a constructor bc we dont have id, db owns id

        entity.setfName(request.fName());
        entity.setlName(request.lName());
        entity.setTeam(request.team());
        entity.setGameDate(request.gameDate());

        entity.setMinutes(request.minutes());
        entity.setPts(request.pts());
        entity.setReb(request.reb());
        entity.setAst(request.ast());

        entity.setFg(request.fg());
        entity.setFga(request.fga());

        entity.setThreeP(request.threeP());
        entity.setThreePa(request.threePa());

        entity.setFt(request.ft());
        entity.setFta(request.fta());

        entity.setSteals(request.stl());
        entity.setBlocks(request.blk());
        entity.setTurnovers(request.tov());
        entity.setPf(request.pf());

        return entity;
    }

    @Override
    public PlayerGameStatResponse toDto(PlayerGameStat entity) {
        return new PlayerGameStatResponse(
                entity.getId(),
                entity.getfName(),
                entity.getlName(),
                entity.getTeam(),
                entity.getGameDate(),
                entity.getPts(),
                entity.getAst(),
                entity.getReb()
        );
    }
}
