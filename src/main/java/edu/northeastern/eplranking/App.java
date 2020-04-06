package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.MatchResult;
import edu.northeastern.eplranking.model.PDModel;
import edu.northeastern.eplranking.model.Scoreboard;
import edu.northeastern.eplranking.model.TeamABMatchResults;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("EPL Ranking System");

        Parent page = FXMLLoader.load(getClass().getResource("/ui/generate_teams_pd.fxml"), null, new JavaFXBuilderFactory());
//        Parent page = FXMLLoader.load(getClass().getResource("/ui/team_vs_x_pd.fxml"), null, new JavaFXBuilderFactory());
        Scene scene = new Scene(page);

        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ParseDataFiles parseDataFiles = new ParseDataFiles();
        parseDataFiles.parse();
        processResults();

        launch(args);
    }


    private void generateGraphFromData(Stage stage) {
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Goals");
        yAxis.setLabel("Probability");
        //creating the chart
        final LineChart<Number, Number> lineChart =
                new LineChart<>(xAxis, yAxis);

        List<PDModel> pdModelList = GameData.getInstance().getPdModelList();

        lineChart.setTitle("Team " + pdModelList.get(100).getTeam1().getName() + " vs Team " + pdModelList.get(100).getTeam2().getName());
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Team");
        //populating the series with data

        series.setData(FXCollections.observableList(pdModelList.get(100).getProbabilityDistribution().entrySet().stream().map(e -> new XYChart.Data<>(e.getKey(), e.getValue())).collect(Collectors.toList())));
//        series.getData().add(new XYChart.Data(1, 23));

        Scene scene = new Scene(lineChart, 800, 600);
        lineChart.getData().add(series);

        stage.setScene(scene);

    }

    private static void processResults() {
        List<MatchResult> matchResults = GameData.getInstance().getMatchResults();
        List<PDModel> pdModelList = GameData.getInstance().getPdModelList();

        List<TeamABMatchResults> teamABMatchResults = new ArrayList<>();
        matchResults.forEach(matchResult -> {
            teamABMatchResults.stream().filter(abResult ->
                    (abResult.getTeam1().equals(matchResult.getHomeTeam()) && abResult.getTeam2().equals(matchResult.getAwayTeam())) ||
                            (abResult.getTeam1().equals(matchResult.getAwayTeam()) && abResult.getTeam2().equals(matchResult.getHomeTeam()))
            ).findAny().ifPresentOrElse(abResult -> {
                if (abResult.getTeam1().equals(matchResult.getHomeTeam())) {
                    abResult.getScoreboardList().add(new Scoreboard(matchResult.getFullTimeHomeScore(), matchResult.getFullTimeAwayScore()));
                } else {
                    abResult.getScoreboardList().add(new Scoreboard(matchResult.getFullTimeAwayScore(), matchResult.getFullTimeHomeScore()));
                }
            }, () -> {
                TeamABMatchResults res = new TeamABMatchResults();
                res.setTeam1(matchResult.getHomeTeam());
                res.setTeam2(matchResult.getAwayTeam());
                res.getScoreboardList().add(new Scoreboard(matchResult.getFullTimeHomeScore(), matchResult.getFullTimeAwayScore()));
                teamABMatchResults.add(res);
            });
        });

        teamABMatchResults.forEach(teamABResult -> {
            Map<Integer, Long> goalDiffProbability = teamABResult.getScoreboardList().stream()
                    .collect(Collectors.groupingBy(Scoreboard::getScoreDiff, Collectors.counting()));

            PDModel pdModel = new PDModel();
            pdModel.setTeam1(teamABResult.getTeam1());
            pdModel.setTeam2(teamABResult.getTeam2());
            pdModel.setProbabilityDistribution(
                    goalDiffProbability.entrySet().stream()
                            .collect(Collectors.toMap(Map.Entry::getKey, e -> (double) e.getValue() / teamABResult.getScoreboardList().size()))
            );
            /*double totalA = 0;
            double totalB = 0;
            for (Scoreboard scoreboard : teamABResult.getScoreboardList()) {
                if (teamABResult.getScoreboardList().indexOf(scoreboard) > 0) {
                    pdModel.getProbabilityDistribution().add(totalA-totalB/totalA+totalB);
                } else {
                    pdModel.getProbabilityDistribution().add(0.5);
                }
                totalA += scoreboard.getFullTimeGoalsA();
                totalB += scoreboard.getFullTimeGoalsB();
            }*/
            pdModelList.add(pdModel);
        });

        /*matchResults.forEach(matchResult -> {
            PDModel model;
            List<MatchResult> m = pdModelList.stream().filter(pdModel ->
                    (pdModel.getTeam1().equals(matchResult.getHomeTeam()) && pdModel.getTeam2().equals(matchResult.getAwayTeam())) ||
                            (pdModel.getTeam1().equals(matchResult.getAwayTeam()) && pdModel.getTeam2().equals(matchResult.getHomeTeam()))
            ).collect(Collectors.toList());



            model = m.orElseGet(PDModel::new);
//            model.getProbabilityDistribution().add();
            pdModelList.add(model);
    });*/

    }
}
