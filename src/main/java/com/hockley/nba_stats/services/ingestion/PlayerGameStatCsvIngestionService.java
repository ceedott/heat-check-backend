package com.hockley.nba_stats.services.ingestion;

import com.hockley.nba_stats.domain.dto.PlayerGameStatRequest;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.mappers.PlayerGameStatMapper;
import com.hockley.nba_stats.repositories.PlayerGameStatRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.logging.log4j.util.Strings.isBlank;

// maybe i should create an interface for this? to follow what i've been doing structurally
@Service
public class PlayerGameStatCsvIngestionService {
    // parse rows
    // validation

    private final PlayerGameStatCsvReader reader;
    private final PlayerGameStatRepository repository;
    private final PlayerGameStatMapper mapper;

    public PlayerGameStatCsvIngestionService(PlayerGameStatCsvReader reader, PlayerGameStatRepository repository, PlayerGameStatMapper mapper) {
        this.reader = reader;
        this.repository = repository;
        this.mapper = mapper;
    }

    // throw io exception in case file not found
    @Transactional
    public IngestionResult ingest(MultipartFile file) throws IOException {
        List<PlayerGameStatCsvRow> rows = reader.read(file.getInputStream());

        List<PlayerGameStatRequest> valid = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        int rowNum = 1;
        for (PlayerGameStatCsvRow row : rows) {
             Optional<PlayerGameStatRequest> parsed = parseRow(row, rowNum, errors);
             if (parsed.isEmpty()) {
                 rowNum++;
                 continue;
             }

             PlayerGameStatRequest req = parsed.get(); // get value out of optional
             boolean isDupe = repository.existsByFNameIgnoreCaseAndLNameIgnoreCaseAndGameDateAndTeam(
                     req.fName(), req.lName(), req.gameDate(), req.team()
             );

             if (isDupe) {
                 errors.add(
                         "Row " + rowNum + ": duplicate "
                                 + req.fName() + " " + req.lName()
                                 + " on " + req.gameDate()
                                 + " (" + req.team() + ")"
                 );
             }
             else {
                 valid.add(req);
             }
             rowNum++;
        }

        List<PlayerGameStat> entities = valid.stream().map(mapper::fromDto).toList();
        repository.saveAll(entities);

        // returning counts, nothing inserted
        return new IngestionResult(
                rows.size(),
                entities.size(),
                rows.size() - entities.size(),
                errors
        );

    }

    @Transactional
    public Optional<PlayerGameStatRequest> parseRow(
            PlayerGameStatCsvRow row,
            int rowNum,
            List<String> errors
    ) {
        if (isBlank(row.fName()) || isBlank(row.lName()) || isBlank(row.gameDate()) || isBlank(row.team())) { // check more
            errors.add("Row " + rowNum + ": missing required fields");
            return Optional.empty();
        }

        LocalDate gameDate = CsvParsingUtils.parseDate(row.gameDate());
        if (gameDate == null || gameDate.isAfter(LocalDate.now())) {
            errors.add("Row " + rowNum + ": invalid game_date");
            return Optional.empty();
        }

        Integer minutes = CsvParsingUtils.parseInt(row.minutes(), "minutes", rowNum, errors);
        Integer pts = CsvParsingUtils.parseInt(row.pts(), "pts", rowNum, errors);
        Integer reb = CsvParsingUtils.parseInt(row.reb(), "reb", rowNum, errors);
        Integer ast = CsvParsingUtils.parseInt(row.ast(), "ast", rowNum, errors);
        Integer fg = CsvParsingUtils.parseInt(row.fg(), "fg", rowNum, errors);
        Integer fga = CsvParsingUtils.parseInt(row.fga(), "fga", rowNum, errors);
        Integer threeP = CsvParsingUtils.parseInt(row.threeP(), "three_p", rowNum, errors);
        Integer threePa = CsvParsingUtils.parseInt(row.threePa(), "three_pa", rowNum, errors);
        Integer ft = CsvParsingUtils.parseInt(row.ft(), "ft", rowNum, errors);
        Integer fta = CsvParsingUtils.parseInt(row.fta(), "fta", rowNum, errors);
        Integer stl = CsvParsingUtils.parseInt(row.stl(), "steals", rowNum, errors);
        Integer blk = CsvParsingUtils.parseInt(row.blk(), "blocks", rowNum, errors);
        Integer tov = CsvParsingUtils.parseInt(row.tov(), "turnovers", rowNum, errors);
        Integer pf = CsvParsingUtils.parseInt(row.pf(), "pf", rowNum, errors);

        if (minutes == null || pts == null || reb == null || ast == null || fg == null || fga == null || threeP == null
        || threePa == null || ft == null || fta == null || stl == null || blk == null || tov == null || pf == null) {
            return Optional.empty();
        }

        // return DTO once everything validated
        return Optional.of(new PlayerGameStatRequest(
                row.fName(),
                row.lName(),
                row.team(),
                gameDate,
                minutes,
                pts,
                reb,
                ast,
                fg,
                fga,
                threeP,
                threePa,
                ft,
                fta,
                stl,
                blk,
                tov,
                pf
        ));

    }

}
