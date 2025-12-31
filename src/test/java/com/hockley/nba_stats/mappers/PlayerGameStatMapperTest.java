package com.hockley.nba_stats.mappers;

import com.hockley.nba_stats.domain.dto.PlayerGameStatRequest;
import com.hockley.nba_stats.domain.dto.PlayerGameStatResponse;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.mappers.impl.PlayerGameStatMapperImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerGameStatMapperTest {

    private final PlayerGameStatMapper mapper = new PlayerGameStatMapperImpl();

    @Test
    void shouldMapRequestDtoToEntity() {
        PlayerGameStatRequest request = new PlayerGameStatRequest(
                "Stephen",
                "Curry",
                "GSW",
                LocalDate.of(2024, 3, 12),
                34,
                30,
                5,
                6,
                10,
                18,
                5,
                11,
                5,
                5,
                2,
                0,
                3,
                2
        );

        PlayerGameStat entity = mapper.fromDto(request);

        assertThat(entity.getId()).isNull(); // JPA owns ID
        assertThat(entity.getfName()).isEqualTo("Stephen");
        assertThat(entity.getlName()).isEqualTo("Curry");
        assertThat(entity.getTeam()).isEqualTo("GSW");
        assertThat(entity.getPts()).isEqualTo(30);
        assertThat(entity.getAst()).isEqualTo(6);
    }

    @Test
    void shouldMapEntityToResponseDto() {
        PlayerGameStat entity = new PlayerGameStat();
        entity.setId(42L);
        entity.setfName("Stephen");
        entity.setlName("Curry");
        entity.setTeam("GSW");
        entity.setGameDate(LocalDate.of(2024, 3, 12));
        entity.setPts(30);
        entity.setAst(6);
        entity.setReb(5);

        PlayerGameStatResponse response = mapper.toDto(entity);

        assertThat(response.id()).isEqualTo(42L);
        assertThat(response.fName()).isEqualTo("Stephen");
        assertThat(response.lName()).isEqualTo("Curry");
        assertThat(response.team()).isEqualTo("GSW");
        assertThat(response.pts()).isEqualTo(30);
    }
}
