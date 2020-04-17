package edu.northeastern.eplranking;

import edu.northeastern.eplranking.ui.ViewFXMLs;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Driver class
 * Loads JavaFX
 */
public class App extends Application {
    /**
     * Load Splash Screen to JavaFX stage -> Controllers can be found in edu.northeaster.eplranking.ui.controller.*
     * Shared Application data stored in {@link edu.northeastern.eplranking.Context}
     * UI Layouts in /resources directory
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("EPL Ranking System");

        Parent page = FXMLLoader.load(getClass().getResource(ViewFXMLs.SPLASH), null, new JavaFXBuilderFactory());
        Scene scene = new Scene(page);

        stage.setScene(scene);
        stage.sizeToScene();
        Context.getInstance().setCurrentStage(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
