package edu.northeastern.eplranking.ui.controller;

import edu.northeastern.eplranking.Context;
import edu.northeastern.eplranking.model.RankingTableModel;
import javafx.beans.binding.Bindings;
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

import java.util.ArrayList;

/**
 * Java FX Controller to display an overall ranking of all teams from within the last 10 years
 * Related View -> ranking_table.fxml
 */
public class RankingTableController {
    private Context app;

    @FXML
    private TableView<RankingTableModel> tblRanking;

    @FXML
    private TableColumn<RankingTableModel, Integer> colRank;

    @FXML
    private TableColumn<RankingTableModel, Image> colLogo;

    @FXML
    private TableColumn<RankingTableModel, String> colTeamName;

    @FXML
    private TableColumn<RankingTableModel, Double> colProbability;

    private ObservableList<RankingTableModel> rankingTableObservableList;

    @FXML
    private void initialize() {
        this.app = Context.getInstance();

        this.rankingTableObservableList = FXCollections.observableList(new ArrayList<>(app.getRankingTable()));
        tblRanking.setItems(rankingTableObservableList);

        colRank.setCellFactory(col -> {
            TableCell<RankingTableModel, Integer> cell = new TableCell<>();
            cell.textProperty().bind(Bindings.createStringBinding(() -> {
                if (cell.isEmpty()) {
                    return null;
                } else {
                    return Integer.toString(cell.getIndex()+1);
                }
            }, cell.emptyProperty(), cell.indexProperty()));
            return cell;
        });

        colLogo.setCellFactory(param -> {
            //Set up the ImageView
            final ImageView imageview = new ImageView();
            imageview.setFitHeight(20);
            imageview.setFitWidth(20);

            //Set up the Table
            TableCell<RankingTableModel, Image> cell = new TableCell<>() {
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
    }

    private void mapTableColumns() {
        colLogo.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(new Image(cellData.getValue().getTeam().getLogoUrl())));
        colTeamName.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTeam().getName()));
        colProbability.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getWinProbability()));
    }

    @FXML
    private void back() {
        app.goBack();
    }
}
