package com.hockley.nba_stats.services.ingestion;

import java.util.List;

public record IngestionResult(
        // returns the log file and stuff
        int totalRows,
        int insertedRows,
        int skippedRows,
        List<String> errors
) {
}
