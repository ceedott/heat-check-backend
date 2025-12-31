package com.hockley.nba_stats.services.ingestion;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CsvParsingUtils {
    public CsvParsingUtils() {
    }

    public static Integer parseInt(String value, String field, int row, List<String> errors) {
        // validation logic, check the int value is present, not null, and >= 0
        if (value == null || value.isBlank()) {
            errors.add("Row " + row + ": missing " + field);
            return null;
        }

        try {
            int val = Integer.parseInt(value);
            if (val >= 0) return val;
            else {
                errors.add("Row " + row + ": invalid " + field + "='" + val + "'");
                return null;
            }
        } catch (NumberFormatException e) {
            errors.add("Row " + row + ": invalid " + field + "='" + value + "'");
            return null;
        }
    }

    public static LocalDate parseDate(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return LocalDate.parse(value);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

}
