package edu.northeastern.eplranking;

import edu.northeastern.eplranking.model.MatchResult;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("EPL Ranking System");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<Number,Number> lineChart =
                new LineChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Team");
        //populating the series with data

        series.getData().add(new XYChart.Data(1, 23));

        series.setData(FXCollections.observableArrayList());
        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws IOException, URISyntaxException {
        ParseDataFiles parseDataFiles = new ParseDataFiles();
        parseDataFiles.parse();
        processResults();

        launch(args);
    }

    private static void processResults() {
        List<MatchResult> matchResults = GameData.getInstance().getMatchResults();


    }
}
