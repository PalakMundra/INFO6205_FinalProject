package edu.northeastern.eplranking.model;

import lombok.Getter;

@Getter
public class Scoreboard {
    private final int fullTimeGoalsA;
    private int fullTimeGoalsB;

    public Scoreboard(int fullTimeGoalsA, int fullTimeGoalsB) {
        this.fullTimeGoalsA = fullTimeGoalsA;
        this.fullTimeGoalsB = fullTimeGoalsB;
    }

    public int getScoreDiff() {
        return fullTimeGoalsA - fullTimeGoalsB;
    }

    public int getTotalGoals() {
        return fullTimeGoalsA + fullTimeGoalsB;
    }
}
