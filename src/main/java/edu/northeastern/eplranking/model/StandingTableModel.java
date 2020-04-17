package edu.northeastern.eplranking.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
public class StandingTableModel implements Comparable<StandingTableModel> {
    private Team team;
    private int wins = 0;
    private int losses = 0;
    private int draws = 0;
    private int goalsFor = 0;
    private int goalsAgainst = 0;

//    private double mean;
//    private double variance;

    public StandingTableModel(StandingTableModel other) {
        this.team = other.team;
        this.wins = other.wins;
        this.losses = other.losses;
        this.draws = other.draws;
        this.goalsFor = other.goalsFor;
        this.goalsAgainst = other.goalsAgainst;
    }

    public int getPoints() {
        return wins * 3 + draws;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getTotalPlayed() {
        return wins + losses + draws;
    }

    @Override
    public int compareTo(StandingTableModel o) {
        int result = Integer.compare(o.getPoints(), this.getPoints());
        if (result != 0) {
            return result;
        }

        result = Integer.compare(o.getGoalDifference(), this.getGoalDifference());
        if (result != 0) {
            return result;
        }

        return Integer.compare(o.goalsFor, this.goalsFor);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StandingTableModel that = (StandingTableModel) o;
        return Objects.equals(team, that.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team);
    }
}
