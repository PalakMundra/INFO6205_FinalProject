package edu.northeastern.eplranking.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class TeamStats {
    private Team team;

    private List<MatchResult> matchResultList;

    private Map<Team, Stats> statsMapVsX = new HashMap<>();



    /*public double getWinProbabilityVsX(Team team) {
        Stats stats = statsMapVsX.get(team);
        double rawProbability = (double) stats.getWins() / stats.getTotalMatches();
        return rawProbability;
    }

    public double getOverallWinProbability(){
        double probability=1;
        GameData.getInstance().getTeamList().forEach(team->{
            if(!this.team.equals(team)){
                if(statsMapVsX.containsKey(team)){

                } else{

                }
            }
        });
        return probability;
    }*/

}
