package com.hockley.nba_stats.ingestion;

import com.hockley.nba_stats.repositories.PlayerGameStatRepository;
import com.hockley.nba_stats.services.ingestion.IngestionResult;
import com.hockley.nba_stats.services.ingestion.PlayerGameStatCsvIngestionService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

@Transactional
@SpringBootTest
public class CSVIngestionServiceTest {

    @Autowired
    private PlayerGameStatCsvIngestionService ingestionService;

    @Autowired
    private PlayerGameStatRepository repository;

    private static final String VALID_CSV = """
f_name,l_name,team,game_date,minutes,pts,reb,ast,fg,fga,three_p,three_pa,ft,fta,steals,blocks,turnovers,pf
LeBron,James,LAL,2024-01-01,38,25,7,8,9,18,2,6,5,6,1,1,4,2
LeBron,James,LAL,2024-01-03,36,30,8,9,11,20,3,7,5,6,2,0,3,1
""";

    private MultipartFile csvFile(String content) {
        return new MockMultipartFile(
                "file",
                "stats.csv",
                "text/csv",
                content.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Test
    void ingest_validCsv_insertsAllRows_andReturnsNoErrors() throws Exception {
        MultipartFile file = csvFile(VALID_CSV);

        IngestionResult result = ingestionService.ingest(file);

        // Assert ingestion summary
        assertEquals(2, result.totalRows());
        assertEquals(2, result.insertedRows());
        assertEquals(0, result.skippedRows());
        assertTrue(result.errors().isEmpty());

        // Assert DB state
        assertEquals(2, repository.count());
    }

}
