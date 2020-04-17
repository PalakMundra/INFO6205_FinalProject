package edu.northeastern.eplranking.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AvsXResult {
    private Team team;

    private List<MatchResult> matchResultList;

    private Map<Team, Stats> statsVsX;

    public double getOverallWinProbability(){

    }
}
