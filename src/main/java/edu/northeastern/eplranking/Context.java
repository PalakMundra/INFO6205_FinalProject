package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.*;
import edu.northeastern.eplranking.ui.ViewFXMLs;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
public class Context {
    private static Context context;
    private Stage currentStage;

    private List<Team> teamList = new ArrayList<>();
    private List<MatchResult> matchResults = new ArrayList<>();

    private List<TeamStats> teamStats = new ArrayList<>();

    private int maxScoreDifference;

    private Map<Team, List<MatchResult>> matchResultMap = new Hashtable<>();

    private List<StandingTableModel> current2019standings;
    private List<StandingTableModel> predicted2019standings;

    private ProbDensity consolidatedProbDensity2019 = new ProbDensity();


    private List<MatchResult> simulated2019Results = new ArrayList<>();

    private Set<RankingTableModel> rankingTable = new TreeSet<>((RankingTableModel o1, RankingTableModel o2) -> Double.compare(o2.getWinProbability(), o1.getWinProbability()));

    private Stack<String> viewStack = new Stack<>();

    public static Context getInstance() {
        if (context == null) {
            context = new Context();
        }
        return context;
    }

    public void goTo2019Standings() {
        try {
            replaceSceneContent(ViewFXMLs.STANDINGS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void goTo2019Results() {
        try {
            replaceSceneContent(ViewFXMLs.SIMULATED_RESULTS);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToProbabilityDistribution() {
        try {
            replaceSceneContent(ViewFXMLs.TEAM_WISE_PROB_DISTRIBUTION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goToRankingTable() {
        try {
            replaceSceneContent(ViewFXMLs.RANKING_TABLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void goBack(){
        viewStack.pop();
        try {
            replaceSceneContent(viewStack.peek());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Parent replaceSceneContent(String fxml) throws Exception {
        viewStack.push(fxml);
        Parent page = FXMLLoader.load(getClass().getResource(fxml), null, new JavaFXBuilderFactory());
        Scene scene = currentStage.getScene();
        if (scene == null) {
            scene = new Scene(page, 1300, Region.USE_COMPUTED_SIZE);
//            scene.getStylesheets().add(getClass().getResource("/css/style.css").toExternalForm());
            currentStage.setScene(scene);
        } else {
            //new FadeOut(currentStage.getScene().getRoot()).setSpeed(0.9).play();
            currentStage.getScene().setRoot(page);
            //new FadeIn(currentStage.getScene().getRoot()).setSpeed(0.9).play();
        }
        currentStage.sizeToScene();
        return page;
    }
}
