package com.hockley.nba_stats.domain.entities;

public enum HeatLevel {
    HOT("hot"),
    COLD("cold"),
    NEUTRAL("neutral");

    private final String column;

    HeatLevel(String column) {
        this.column = column;
    }

    public String getColumn() {
        return column;
    }

}
