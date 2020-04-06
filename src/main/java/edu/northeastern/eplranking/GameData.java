package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.MatchResult;
import edu.northeastern.eplranking.model.PDModel;
import edu.northeastern.eplranking.model.Team;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GameData {
    private static GameData gameData;

    private List<Team> teamList = new ArrayList<>();
    private List<MatchResult> matchResults = new ArrayList<>();
    private List<PDModel> pdModelList = new ArrayList<>();

    private GameData() {
    }

    public static GameData getInstance() {
        if (gameData == null) {
            gameData = new GameData();
        }
        return gameData;
    }

}
