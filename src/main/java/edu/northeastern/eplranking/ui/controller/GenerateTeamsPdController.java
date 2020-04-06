package edu.northeastern.eplranking.ui.controller;

import edu.northeastern.eplranking.GameData;
import edu.northeastern.eplranking.model.PDModel;
import edu.northeastern.eplranking.model.Team;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GenerateTeamsPdController {
    @FXML
    private ComboBox<Team> comboTeam1;

    @FXML
    private ComboBox<Team> comboTeam2;

    @FXML
    private BarChart<String, Double> pdChart;


    @FXML
    private void initialize() {
        List<Team> teamList = GameData.getInstance().getTeamList();
        comboTeam1.setItems(FXCollections.observableArrayList(teamList));
        comboTeam2.setItems(FXCollections.observableArrayList(teamList));

        pdChart.setTitle("Probability Distribution");
        pdChart.setLegendVisible(true);
    }


    @FXML
    private void generate() {
        Team selectedTeam1 = comboTeam1.getSelectionModel().getSelectedItem();
        Team selectedTeam2 = comboTeam2.getSelectionModel().getSelectedItem();

        List<PDModel> pdModelList = GameData.getInstance().getPdModelList();

        Optional<PDModel> model = pdModelList.stream().filter(pdModel -> pdModel.getTeam1().equals(selectedTeam1) && pdModel.getTeam2().equals(selectedTeam2)).findAny();
        if (model.isEmpty()) {
            model = pdModelList.stream().filter(pdModel -> pdModel.getTeam1().equals(selectedTeam2) && pdModel.getTeam2().equals(selectedTeam1)).findAny().map(oldPdModel -> {
                PDModel inverted = new PDModel();
                inverted.setTeam1(oldPdModel.getTeam2());
                inverted.setTeam2(oldPdModel.getTeam1());
                inverted.setProbabilityDistribution(oldPdModel.getProbabilityDistribution().entrySet().stream().collect(Collectors.toMap(integerDoubleEntry -> integerDoubleEntry.getKey()*-1, e -> e.getValue())));
                return inverted;
            });
        }

        if (model.isEmpty()) {
            return;
        }



        XYChart.Series<String, Double> series = new XYChart.Series<>();
        series.setData(FXCollections.observableArrayList(model.get().getProbabilityDistribution().entrySet().stream().map(e -> new XYChart.Data<>(String.valueOf(e.getKey()), e.getValue())).collect(Collectors.toList())));
        series.setName("Team " + selectedTeam1.getName() + " vs Team " + selectedTeam2.getName());
        pdChart.getData().add(series);
    }

}
