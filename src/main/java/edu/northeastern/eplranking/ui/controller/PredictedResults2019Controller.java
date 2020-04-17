package edu.northeastern.eplranking.ui.controller;

import edu.northeastern.eplranking.Context;
import edu.northeastern.eplranking.model.MatchResult;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;


/**
 * Java FX Controller to display simulated results for the stalled matches from the 2019 season
 * Related View -> results_2019.fxml
 */
public class PredictedResults2019Controller {
    @FXML
    private TableView<MatchResult> tblPredictedResults;

    @FXML
    private TableColumn<MatchResult, String> colMatchDate;

    @FXML
    private TableColumn<MatchResult, String> colHomeTeamName;

    @FXML
    private TableColumn<MatchResult, String> colAwayTeamName;

    @FXML
    private TableColumn<MatchResult, Integer> colFullTimeHomeGoals;

    @FXML
    private TableColumn<MatchResult, Integer> colFullTimeAwayGoals;

    private ObservableList<MatchResult> simulatedMatchResultsObservableList;

    private Context app;

    @FXML
    private void initialize(){
        this.app = Context.getInstance();
        simulatedMatchResultsObservableList = FXCollections.observableList(app.getSimulated2019Results());

        tblPredictedResults.setItems(simulatedMatchResultsObservableList);
        mapTableColumns();

    }

    private void mapTableColumns() {
        colMatchDate.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getMatchDateTime().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm"))));
        colHomeTeamName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getHomeTeam().getName()));
        colAwayTeamName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAwayTeam().getName()));
        colFullTimeHomeGoals.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFullTimeHomeScore()));
        colFullTimeAwayGoals.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getFullTimeAwayScore()));
    }

    @FXML
    private void back() {
        app.goBack();
    }
}
