package edu.northeastern.eplranking.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MatchResult {
    private LocalDate matchDate;

    private Team homeTeam;
    private Team awayTeam;

    private int fullTimeHomeScore;
    private int fullTimeAwayScore;

    private int halfTimeHomeScore;
    private int halfTimeAwayScore;
}
