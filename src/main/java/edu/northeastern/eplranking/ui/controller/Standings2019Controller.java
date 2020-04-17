package edu.northeastern.eplranking.ui.controller;

import edu.northeastern.eplranking.Context;
import edu.northeastern.eplranking.model.StandingTableModel;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.List;

/**
 * Java FX Controller to display the current and predicted standings for 2019
 * Related View -> standings_2019.fxml
 */
public class Standings2019Controller {
    @FXML
    private TableView<StandingTableModel> tblCurrentStandings;

    @FXML
    private TableView<StandingTableModel> tblPredictedStandings;

    @FXML
    private TableColumn<StandingTableModel, String> colCurrentClub;

    @FXML
    private TableColumn<StandingTableModel, Image> colCurrentLogo;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentPlayed;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentWins;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentLosses;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentDraws;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentGoalsFor;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentGoalsAgainst;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentGoalDifference;

    @FXML
    private TableColumn<StandingTableModel, Integer> colCurrentPoints;

    @FXML
    private TableColumn<StandingTableModel, String> colPredictedClub;

    @FXML
    private TableColumn<StandingTableModel, Image> colPredictedLogo;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedPlayed;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedWins;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedLosses;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedDraws;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedGoalsFor;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedGoalsAgainst;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedGoalDifference;

    @FXML
    private TableColumn<StandingTableModel, Integer> colPredictedPoints;

    @FXML
    private Text txtCertainty;


    private ObservableList<StandingTableModel> currentStandingsObservableList;

    private ObservableList<StandingTableModel> predictedStandingsObservableList;

    private Context app;

    @FXML
    private void initialize() {
        app = Context.getInstance();

        List<StandingTableModel> currentStandingsTable = Context.getInstance().getCurrent2019standings();
        List<StandingTableModel> predictedStandingsTable = Context.getInstance().getPredicted2019standings();
        this.currentStandingsObservableList = FXCollections.observableList(currentStandingsTable);
        this.predictedStandingsObservableList = FXCollections.observableList(predictedStandingsTable);
        tblCurrentStandings.setItems(currentStandingsObservableList);
        tblPredictedStandings.setItems(predictedStandingsObservableList);
        colCurrentLogo.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(20);
            imageview.setFitWidth(20);

            //Set up the Table
            TableCell<StandingTableModel, Image> cell = new TableCell<>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(imageview);
            return cell;
        });

        colPredictedLogo.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(20);
            imageview.setFitWidth(20);

            //Set up the Table
            TableCell<StandingTableModel, Image> cell = new TableCell<>() {
                public void updateItem(Image item, boolean empty) {
                    if (item != null) {
                        imageview.setImage(item);
                    }
                }
            };
            // Attach the imageview to the cell
            cell.setGraphic(imageview);
            return cell;
        });
        mapTableColumns();

        /*ProbDensity consolidated = app.getConsolidatedProbDensity2019();
        Analyser analyser = new Analyser();
        double certainty = analyser.getAreaUnderTheCurve(consolidated.getMean(),consolidated.getVariance(),Math.sqrt(consolidated.getVariance()), 0.1, 250) * 100;
        txtCertainty.setText(certainty + "%");*/
    }

    private void mapTableColumns() {

        colCurrentLogo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(new Image(cellData.getValue().getTeam().getLogoUrl())));
        colCurrentClub.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTeam().getName()));
        colCurrentPlayed.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalPlayed()));
        colCurrentWins.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getWins()));
        colCurrentLosses.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLosses()));
        colCurrentDraws.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDraws()));
        colCurrentGoalsFor.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalsFor()));
        colCurrentGoalsAgainst.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalsAgainst()));
        colCurrentGoalDifference.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalDifference()));
        colCurrentPoints.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPoints()));


        colPredictedLogo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(new Image(cellData.getValue().getTeam().getLogoUrl())));
        colPredictedClub.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTeam().getName()));
        colPredictedPlayed.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getTotalPlayed()));
        colPredictedWins.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getWins()));
        colPredictedLosses.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getLosses()));
        colPredictedDraws.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDraws()));
        colPredictedGoalsFor.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalsFor()));
        colPredictedGoalsAgainst.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalsAgainst()));
        colPredictedGoalDifference.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getGoalDifference()));
        colPredictedPoints.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getPoints()));
    }


    @FXML
    private void viewSimulatedResults() {
        app.goTo2019Results();
    }

    @FXML
    private void viewProbabilityDistributions(){
        app.goToProbabilityDistribution();
    }


    @FXML
    private void viewOverallRankings(){
        app.goToRankingTable();
    }
}
