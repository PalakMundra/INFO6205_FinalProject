package edu.northeastern.eplranking.ui.controller;

import edu.northeastern.eplranking.Analyser;
import edu.northeastern.eplranking.Context;
import edu.northeastern.eplranking.model.ProbDensity;
import edu.northeastern.eplranking.model.Stats;
import edu.northeastern.eplranking.model.Team;
import edu.northeastern.eplranking.model.TeamStats;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.text.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Java FX Controller to display Team A vs Team B probability distributions of goal differences
 * Related View -> generate_teams_pd.fxml
 */
public class GenerateTeamsPdController {
    @FXML
    private ComboBox<Team> comboTeam1;

    @FXML
    private ComboBox<Team> comboTeam2;

    @FXML
    private Label lblOverallWinProbability;

    @FXML
    private Label lblOverallLossProbability;

    @FXML
    private Label lblOverallDrawProbability;

    @FXML
    private Text txtOverallWinProbability;

    @FXML
    private Text txtOverallLossProbability;

    @FXML
    private Text txtOverallDrawProbability;

    @FXML
    private BarChart<String, Double> pdChart;

    private Context app;

    @FXML
    private void initialize() {
        app = Context.getInstance();
        List<Team> teamList = Context.getInstance().getTeamList();
        comboTeam1.setItems(FXCollections.observableArrayList(teamList));
        comboTeam2.setItems(FXCollections.observableArrayList(teamList));

        pdChart.setTitle("Probability Distribution");
        pdChart.getXAxis().setLabel("Goal Difference");
        pdChart.getYAxis().setLabel("Probability");
        pdChart.setLegendVisible(true);
        pdChart.getXAxis().setAnimated(false);

    }


    @FXML
    private void generate() {
        Team selectedTeam1 = comboTeam1.getSelectionModel().getSelectedItem();
        Team selectedTeam2 = comboTeam2.getSelectionModel().getSelectedItem();

        if (selectedTeam1 == null || selectedTeam2 == null) {
            new Alert(Alert.AlertType.WARNING, "Please select both teams").show();
            return;
        }

        if (selectedTeam1.equals(selectedTeam2)) {
            new Alert(Alert.AlertType.WARNING, "Please select different teams").show();
            return;
        }


        List<TeamStats> teamStatsList = app.getTeamStats();

        Stats stats = teamStatsList.stream().filter(teamStats -> teamStats.getTeam().equals(selectedTeam1)).findAny().get().getStatsMapVsX().get(selectedTeam2);
        ProbDensity probabilityDensity = stats.getProbDensity();
        lblOverallWinProbability.setText(selectedTeam1 + " Win Probability: ");
        lblOverallLossProbability.setText(selectedTeam1 + " Loss Probability: ");
        lblOverallDrawProbability.setText("Draw Probability: ");
        Analyser analyser = new Analyser();
        Double winProbability = null;
        Double lossProbability = null;
        Double drawProbability = null;
        try {
            //Arbitrary end value used for the normal curve (because the real distribution would not be really normal)
            winProbability = analyser.getAreaUnderTheCurve(probabilityDensity.getMean(), probabilityDensity.getVariance(), probabilityDensity.getStandardDeviation(), 0.5, 150) * 100;
            lossProbability = analyser.getAreaUnderTheCurve(probabilityDensity.getMean(), probabilityDensity.getVariance(), probabilityDensity.getStandardDeviation(), -150, -0.6) * 100;
            drawProbability = 100 - winProbability - lossProbability;
        } catch (Exception e) {

        }
        if (winProbability != null) {
            txtOverallWinProbability.setText(String.format("%.2f", winProbability) + "%");
        } else {
            txtOverallWinProbability.setText("Could not compute");
        }

        if (lossProbability != null) {
            txtOverallLossProbability.setText(String.format("%.2f", lossProbability) + "%");
        } else {
            txtOverallLossProbability.setText("Could not compute");
        }

        if (drawProbability != null) {
            txtOverallDrawProbability.setText(String.format("%.2f", drawProbability) + "%");
        } else {
            txtOverallDrawProbability.setText("Could not compute");
        }


        Map<Integer, Double> model = probabilityDensity.getGoalDiffProbabilityMap();


        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setData(FXCollections.observableArrayList(model.entrySet().stream().map(e -> new XYChart.Data<>(String.valueOf(e.getKey()), e.getValue())).collect(Collectors.toList())));
        series.setName("Team " + selectedTeam1.getName() + " vs Team " + selectedTeam2.getName() + " Mean: " + stats.getProbDensity().getMean());
        pdChart.setData(FXCollections.singletonObservableList(series));
    }

    @FXML
    private void back() {
        app.goBack();
    }

}
