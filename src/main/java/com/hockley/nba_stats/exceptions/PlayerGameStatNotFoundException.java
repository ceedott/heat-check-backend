package com.hockley.nba_stats.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) // automatically maps to 404
public class PlayerGameStatNotFoundException extends RuntimeException{
    public PlayerGameStatNotFoundException(Long id) {
        super("PlayerGameStat not found with id: " + id);
    }

    public PlayerGameStatNotFoundException(String firstName, String lastName) {
        super("PlayerGameStat not found for player: " + firstName + " " + lastName);
    }

}
