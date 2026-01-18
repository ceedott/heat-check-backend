package com.hockley.nba_stats.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlayerNotFoundException extends RuntimeException {
    public PlayerNotFoundException(String message) {
        super(message);
    }
    public PlayerNotFoundException(String first, String last){
        super("Player not found with first name: '" + first + "' and last name: '" + last + "'");
    }
    public PlayerNotFoundException(Long playerId){
        super("Player not found with player ID: " + playerId);
    }
}
