package com.hockley.nba_stats.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GameLogNotFoundException extends RuntimeException {

    public GameLogNotFoundException(String message) {
        super(message);
    }

    public GameLogNotFoundException(Long playerId) {
        super("Game logs not found for player id: " + playerId);
    }
}
