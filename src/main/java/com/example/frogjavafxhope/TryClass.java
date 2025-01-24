/*
package com.example.frogjavafxhope;



import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.RIGHT;


public class GameController {



    @FXML
    private ImageView frog;



    @FXML
    private ImageView lily;

    @FXML
    private Pane mainPane;

    private final ArrayList<LilyImageView> lilies = new ArrayList<>();
    private boolean moveLeft = false;
    private boolean moveRight = false;
    private boolean isJumpDone = true;
    private final AtomicInteger doubleJump = new AtomicInteger();
    private Timeline leftTimeline;
    private Timeline rightTimeline;

    @FXML
    private ImageView lilyScore;

    @FXML
    private Label score;

    private static int scoreInt = 0;
    private final static Image image1 = new Image(
            "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/LilyPink.png");
    private final static Random random = new Random();

    private int width = 600;
    private int height = 400;
    public static final int SIZE = 30;

    public static int getScore() {
        return scoreInt;
    }


    @FXML
    void initialize() {
        Platform.runLater(() -> mainPane.requestFocus());

        scoreInt = 0;
        Image image = new Image(
                "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/frog.png");
        frog.setImage(image);
        frog.setFitWidth(32.0);
        frog.setFitHeight(43.0);
        Image imageGold = new Image(
                "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/LilyGold.png");

        lilyScore.setImage(imageGold);

        lily.setImage(imageGold);
        lily.setLayoutY(random.nextInt(width - SIZE) + 1);
        lily.setLayoutX(random.nextInt(height - SIZE) + 1);

        mainPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            // Обновляем ширину в переменной
            width = newValue.intValue();
            System.out.println("Обновленная ширина: " + width);
        });

        mainPane.heightProperty().addListener((observable, oldValue, newValue) -> {
            // Обновляем высоту в переменной
            height = newValue.intValue();
            System.out.println("Обновленная высота: " + height);
        });



        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.00225), event -> {


            if (frog.getLayoutY() >= width + 10) {
                timeline.stop();
                frog.getScene().getWindow().hide();
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/com/example/frogjavafxhope/lose-hope.fxml"));
                try {
                    loader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Parent root = loader.getRoot();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();
            }

            boolean isFalling = true;
            for (LilyImageView flower: lilies) {
                Line line = new Line(frog.getLayoutX(), frog.getLayoutY() + 42, frog.getLayoutX() + 32, frog.getLayoutY() + 42);
                if (line.getBoundsInParent().intersects(flower.getLily().getBoundsInParent())) isFalling = false;
            }
            if (isFalling && isJumpDone) {
                frog.setLayoutY(frog.getLayoutY() + 0.5);
                doubleJump.set(0);
            }
            if (frog.getBoundsInParent().intersects(lily.getBoundsInParent())) {
                score.setText(String.valueOf(Integer.parseInt(score.getText()) + 1));
                scoreInt += 1;
                lily.setLayoutY(random.nextInt(width - SIZE) + 1);
                lily.setLayoutX(random.nextInt(height - SIZE) + 1);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        System.out.println();

        ArrayList<LilyImageView> firstLilies = new ArrayList<>();
        int amountFlowersWidth = width / 60;
        int amountFlowersHeight = height / 100;
        System.out.println("amountFlowersWidth: " + amountFlowersWidth + "; amountFlowersHeight " + amountFlowersHeight);

        for (int j = 1; j <= amountFlowersHeight; j += 2) {
            for (int i = 1; i <= amountFlowersWidth; i++) {
                createNewFlower(random.nextInt(width / amountFlowersWidth) + width / amountFlowersWidth * i,
                        random.nextInt(height / amountFlowersHeight) + height / amountFlowersHeight * j,
                        i * width / amountFlowersWidth - width / amountFlowersWidth + 1,
                        random.nextInt(height / amountFlowersHeight) - height / amountFlowersHeight * j,
                        i * width / amountFlowersWidth, j * height / amountFlowersHeight,
                        firstLilies, 30);
            }
        }
        System.out.println("Width: " + width + "; Height " + height);
        firstLilies.get(4).getLily().setLayoutY(207);
        firstLilies.get(4).getLily().setLayoutX(285);

        ArrayList<LilyImageView> secondLilies = new ArrayList<>();
        for (int j = 2; j <= amountFlowersHeight; j += 2) {
            for (int i = 1; i <= amountFlowersWidth; i++) {
                createNewFlower(random.nextInt(width / amountFlowersWidth) + width / amountFlowersWidth * i,
                        random.nextInt(height / amountFlowersHeight) + height / amountFlowersHeight * j,
                        i * width / amountFlowersWidth - width / amountFlowersWidth + 1,
                        random.nextInt(height / amountFlowersHeight) - height / amountFlowersHeight * j,
                        i * width / amountFlowersWidth, j * height / amountFlowersHeight,
                        secondLilies, 30);
            }
        }


        liliesGrowing(firstLilies);

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    liliesGrowing(secondLilies);
                })
        );
        timeline1.setCycleCount(1); // Запускать только один раз
        timeline1.play();
 */
