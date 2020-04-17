package edu.neu.javachip.aedfinalproject.ui;

import animatefx.animation.Bounce;
import animatefx.animation.RotateIn;
import animatefx.animation.RotateOut;
import edu.neu.javachip.aedfinalproject.ui.model.Context;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

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

    private Context app;

    @FXML
    public void initialize(){

        app = Context.getInstance();

        bgMask.setEffect(new GaussianBlur(10));

        new Bounce(circle1).setCycleCount(4).setDelay(Duration.valueOf("600ms")).play();
        new Bounce(circle2).setCycleCount(4).setDelay(Duration.valueOf("1200ms")).play();
        new Bounce(circle3).setCycleCount(4).setDelay(Duration.valueOf("1300ms")).play();
        new Bounce(circle4).setCycleCount(4).setDelay(Duration.valueOf("800ms")).play();
        new Bounce(circle5).setCycleCount(4).setDelay(Duration.valueOf("1500ms")).play();

        new RotateOut(circleBig1).setCycleCount(2).setSpeed(0.4).setDelay(Duration.valueOf("700ms")).play();
        new RotateIn(circleBig2).setCycleCount(2).setSpeed(0.5).setDelay(Duration.valueOf("1400ms")).play();
        new RotateOut(circleBig3).setCycleCount(2).setSpeed(0.4).setDelay(Duration.valueOf("1600ms")).play();

        PauseTransition pause = new PauseTransition(Duration.seconds(6));
        pause.setOnFinished(event -> {
            app.gotoLogin();
        });
        pause.play();
    }
}
