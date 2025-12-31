package com.hockley.nba_stats.services.impl;

import com.hockley.nba_stats.domain.dto.PlayerAveragesResponse;
import com.hockley.nba_stats.domain.dto.RollingAverage;
import com.hockley.nba_stats.domain.entities.PlayerGameStat;
import com.hockley.nba_stats.domain.entities.StatType;
import com.hockley.nba_stats.exceptions.PlayerGameStatNotFoundException;
import com.hockley.nba_stats.repositories.PlayerGameStatRepository;
import com.hockley.nba_stats.services.PlayerGameStatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class PlayerGameStatServiceImpl implements PlayerGameStatService {

    private final PlayerGameStatRepository repository;

    public PlayerGameStatServiceImpl(PlayerGameStatRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PlayerGameStat> listPlayerGameStats() {
        return repository.findAll();
    }

    @Override
    public PlayerGameStat createPlayerGameStat(PlayerGameStat playerGameStat) {
        // make sure id is null
        if (playerGameStat.getId() != null){
            throw new IllegalArgumentException("Game Log already has an ID");
        }
        if (playerGameStat.getGameDate().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Game date cannot be in the future");
        }

        // no need to validate duplicates, db already does that with UNIQUE constraint

        // dont need to validate anything else bc we do so already in the dto record

        //save game log to repository
        return repository.save(playerGameStat);
    }

    @Override
    public PlayerGameStat getPlayerGameStat(Long playerGameStatId) {
        return repository.findById(playerGameStatId).orElseThrow(() -> new PlayerGameStatNotFoundException(playerGameStatId));
    }

    @Override
    public PlayerGameStat updatePlayerGameStat(Long playerGameStatId, PlayerGameStat new_stat) {
        PlayerGameStat old_stat = this.getPlayerGameStat(playerGameStatId);

        // update the stat, never update id
        old_stat.setfName(new_stat.getfName());
        old_stat.setlName(new_stat.getlName());
        old_stat.setTeam(new_stat.getTeam());
        old_stat.setGameDate(new_stat.getGameDate());
        old_stat.setMinutes(new_stat.getMinutes());
        old_stat.setPts(new_stat.getPts());
        old_stat.setReb(new_stat.getReb());
        old_stat.setAst(new_stat.getAst());
        old_stat.setFg(new_stat.getFg());
        old_stat.setFga(new_stat.getFga());
        old_stat.setThreeP(new_stat.getThreeP());
        old_stat.setThreePa(new_stat.getThreePa());
        old_stat.setFt(new_stat.getFt());
        old_stat.setFta(new_stat.getFta());
        old_stat.setSteals(new_stat.getSteals());
        old_stat.setBlocks(new_stat.getBlocks());
        old_stat.setTurnovers(new_stat.getTurnovers());
        old_stat.setPf(new_stat.getPf());
        return repository.save(old_stat);
    }

    @Override
    public void deletePlayerGameStat(Long playerGameStatId) {
        this.getPlayerGameStat(playerGameStatId); // probably not optimal but whatever. using to check existence
        repository.deleteById(playerGameStatId);
    }

    @Override
    public Page<PlayerGameStat> getTopByStat(StatType statType, Pageable pageable) {
        Pageable sortedPageTable = switch (statType) {
            // create new implementation of pageable, cannot modify old one bc pageable is immutable
            case PTS -> PageRequest.of(
                    pageable.getPageNumber(), // get page number client requested, if they requested a specific page
                    pageable.getPageSize(), // however many results the user asked for, keep that
                    Sort.by("pts").descending() // ignore any sort user sent, we want to sort by pts DESC
            );
            case REB -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("reb").descending()
            );
            case AST -> PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("ast").descending()
            );
        };

        // returns a page. contains list of results, total # rows, total pages, page num, is first/last page
        return repository.findAll(sortedPageTable);
    }

    @Override
    public Page<PlayerGameStat> getByfNameAndlName(String fName, String lName, Pageable pageable) {
        return repository.findByFNameIgnoreCaseAndLNameIgnoreCase(fName, lName, pageable);
    }

    @Override
    public List<PlayerAveragesResponse> getPlayerAverages(String first, String last) {
        // its okay to return a response dto here because this is a query/data dto not a transport dto
        List<PlayerAveragesResponse> results = repository.getPlayerAverages(first, last);

        // if list empty, player not found. they have no game logs
        if (results.isEmpty()) {
            throw new PlayerGameStatNotFoundException(first, last);
        }

        return results;
    }

    @Override
    public List<RollingAverage> getPlayerRollingAverageByDate(String first, String last, LocalDate from, LocalDate to, StatType statType, int window) {
        List<PlayerGameStat> games = repository.findByFNameIgnoreCaseAndLNameIgnoreCaseAndGameDateBetweenOrderByGameDate(first, last, from, to);

        List<RollingAverage> rollingAverages = new ArrayList<>();
        Deque<Integer> q = new ArrayDeque<>();
        int runningSum = 0;

        for (PlayerGameStat game : games) {
            int stat = switch (statType) {
                case PTS -> game.getPts();
                case REB -> game.getReb();
                case AST -> game.getAst();
            };

            q.addLast(stat);
            runningSum += stat;
            if (q.size() > window) {
                runningSum -= q.removeFirst();
            }

            double avg = runningSum / (double)q.size();

            rollingAverages.add(new RollingAverage(
                    game.getGameDate(),
                    stat,
                    avg
            ));
        }

        return rollingAverages;

    }

    @Override
    public List<RollingAverage> getPlayerRollingAverage(String first, String last, StatType statType, int gamesAmt, int window) {
        Pageable limit = PageRequest.of(0, gamesAmt); // page 0, gameAmt page size
        List<PlayerGameStat> games = repository.findRecentGames(first, last, limit);

        if (games.isEmpty()) {
            return List.of(); // no games, return empty list
        }

        // reverse so we process oldest to newest
        Collections.reverse(games);

        List<RollingAverage> rollingAverages = new ArrayList<>();
        Deque<Integer> q = new ArrayDeque<>();
        int runningSum = 0;

        for (PlayerGameStat game : games) {
            int stat = switch (statType) {
                case PTS -> game.getPts();
                case REB -> game.getReb();
                case AST -> game.getAst();
            };

            q.addLast(stat);
            runningSum += stat;
            if (q.size() > window) {
                runningSum -= q.removeFirst();
            }

            double avg = runningSum / (double)q.size();

            rollingAverages.add(new RollingAverage(
                    game.getGameDate(),
                    stat,
                    avg
            ));
        }

        return rollingAverages;

    }


}
