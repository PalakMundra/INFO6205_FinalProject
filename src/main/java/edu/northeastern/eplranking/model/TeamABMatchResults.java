package edu.northeastern.eplranking.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamABMatchResults {
    private Team team1;
    private Team team2;

    private List<Scoreboard> scoreboardList = new ArrayList<>();
}
