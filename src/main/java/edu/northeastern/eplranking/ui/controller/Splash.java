package edu.northeastern.eplranking.ui.controller;

import animatefx.animation.Bounce;
import animatefx.animation.RotateIn;
import animatefx.animation.RotateOut;
import edu.northeastern.eplranking.Analyser;
import edu.northeastern.eplranking.Context;
import edu.northeastern.eplranking.ParseDataFiles;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Java FX Controller for the splash screen
 * Related View -> splash.fxml
 */
public class Splash {
    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    @FXML
    private Circle circle4;

    @FXML
    private Circle circle5;

    @FXML
    private Circle circleBig1;

    @FXML
    private Circle circleBig2;

    @FXML
    private Circle circleBig3;

    @FXML
    private AnchorPane bgMask;

    @FXML
    private Text txtStatus;

    private Context app;

    @FXML
    public void initialize() {

        app = Context.getInstance();

        bgMask.setEffect(new GaussianBlur(10));

        new Bounce(circle1).setCycleCount(8).setDelay(Duration.valueOf("600ms")).play();
        new Bounce(circle2).setCycleCount(8).setDelay(Duration.valueOf("1200ms")).play();
        new Bounce(circle3).setCycleCount(8).setDelay(Duration.valueOf("1300ms")).play();
        new Bounce(circle4).setCycleCount(8).setDelay(Duration.valueOf("800ms")).play();
        new Bounce(circle5).setCycleCount(8).setDelay(Duration.valueOf("1500ms")).play();

        new RotateOut(circleBig1).setCycleCount(4).setSpeed(0.4).setDelay(Duration.valueOf("700ms")).play();
        new RotateIn(circleBig2).setCycleCount(4).setSpeed(0.5).setDelay(Duration.valueOf("1400ms")).play();
        new RotateOut(circleBig3).setCycleCount(4).setSpeed(0.4).setDelay(Duration.valueOf("1600ms")).play();


        txtStatus.setText("Reading Data");

        //Extremely quick and dirty hack for giving textual pauses without hanging the UI thread in JavaFX
        //Please do not judge me on this 😁
        new Thread(() -> {
            ParseDataFiles parseDataFiles = new ParseDataFiles();
            try {
                Thread.sleep(1000);
                parseDataFiles.parse();
                Platform.runLater(() -> {
                    txtStatus.setText("Successfully Loaded Data from files");
                    new Thread(() -> {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Platform.runLater(() -> {
                            txtStatus.setText("Generating Probability Densities");
                            new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                Analyser analyser = new Analyser();
                                analyser.buildPdf();
                                Platform.runLater(() -> {
                                    txtStatus.setText("Consolidating present 2019 standings");

                                    new Thread(() -> {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        analyser.build2019Standings();
                                        Platform.runLater(() -> {
                                            txtStatus.setText("Simulating Matches");
                                            new Thread(() -> {
                                                try {
                                                    Thread.sleep(1000);
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                                app.setPredicted2019standings(analyser.predict2019Standings());
                                                analyser.generateOverallRankings();
                                                Platform.runLater(() -> {
                                                    txtStatus.setText("Successfully Loaded Results");
                                                    PauseTransition pause = new PauseTransition(Duration.seconds(0.01));
                                                    pause.setOnFinished(event -> {
                                                        app.goTo2019Standings();
                                                    });
                                                    pause.play();
                                                });
                                            }).start();
                                        });
                                    }).start();
                                });
                            }).start();
                        });
                    }).start();
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        /*PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(event -> {
            //app.gotoLogin();
        });
        pause.play();*/
    }
}
