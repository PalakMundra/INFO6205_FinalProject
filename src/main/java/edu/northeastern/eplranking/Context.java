package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class GameData {
    private static GameData gameData;

    private List<Team> teamList = new ArrayList<>();
    private List<MatchResult> matchResults = new ArrayList<>();

    private List<TeamStats> teamStats = new ArrayList<>();

    private int maxScoreDifference;

    private Map<Team, List<MatchResult>> matchResultMap = new Hashtable<>();

    private List<StandingTableModel> current2019standings;
    private List<StandingTableModel> predicted2019standings;

    private Set<RankingTableModel> rankingTable = new TreeSet<>((RankingTableModel o1, RankingTableModel o2) -> Double.compare(o2.getWinProbability(), o1.getWinProbability()));

    public static GameData getInstance() {
        if (gameData == null) {
            gameData = new GameData();
        }
        return gameData;
    }

}
