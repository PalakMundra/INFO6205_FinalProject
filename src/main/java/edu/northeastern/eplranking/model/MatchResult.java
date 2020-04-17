package edu.northeastern.eplranking.model;

import lombok.Data;

import java.time.LocalDateTime;

/*private final static Map<String, String> fixtureStatus = Map.of(
        "TBD", "Time To Be Defined",
        "NS", "Not Started",
        "1H", "First Half, Kick Off",
        "HT", "Halftime",
        "2H", "Second Half, 2nd Half Started",
        "ET", "Extra Time",
        "P", "Penalty In Progress",
        "FT", "Match Finished",
        "AET", "Match Finished After Extra Time",
        "PEN", "Match Finished After Penalty",
        "BT", "Break Time (in Extra Time)"
        );*/

@Data
public class MatchResult {
    //private LocalDate matchDate;
    private LocalDateTime matchDateTime;

    private int season;

    private Team homeTeam;
    private Team awayTeam;

    private int fullTimeHomeScore;
    private int fullTimeAwayScore;

    private String status;

    private int halfTimeHomeScore;
    private int halfTimeAwayScore;


    /**
     * Home goals - away goals
     * @return
     */
    public int getFullTimeGoalDifference() {
        return fullTimeHomeScore - fullTimeAwayScore;
    }

}
