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
import javafx.scene.paint.Color;
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
    private final static Image image1 = new Image(
            "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/LilyPink.png");
    private final static Random random = new Random();


    @FXML
    void initialize() {
        Platform.runLater(() -> mainPane.requestFocus());
        Image image = new Image(
                "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/frog.png");
        frog.setImage(image);
        frog.setFitWidth(32.0);
        frog.setFitHeight(43.0);
        Image imageGold = new Image(
                "file:///Users/ulianaboikova/IdeaProjects/FrogJavaFxHope/src/main/java/com/example/frogjavafxhope/assets/LilyGold.png");

        lilyScore.setImage(imageGold);

        lily.setImage(imageGold);
        lily.setLayoutY(random.nextInt(370) + 1);
        lily.setLayoutX(random.nextInt(570) + 1);



        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.0025), event -> {


            if (frog.getLayoutY() >= 410) {
                timeline.stop();
                System.out.println("You win!");
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
            }
            if (frog.getBoundsInParent().intersects(lily.getBoundsInParent())) {
                score.setText(String.valueOf(Integer.parseInt(score.getText()) + 1));
                lily.setLayoutY(random.nextInt(370) + 1);
                lily.setLayoutX(random.nextInt(570) + 1);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
        System.out.println();

        ArrayList<LilyImageView> firstLilies = new ArrayList<>();

        for (int i = 1; i <= 10; i++) {
            createNewFlower(i * 60 - 60 + 1, 201,i * 60 - 60 + 1, 1, i * 60, 200, firstLilies, 30);
        }


        ArrayList<LilyImageView> secondLilies = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            createNewFlower(i * 60 - 60 + 1, 1, i * 60 - 60 + 1, 201, i * 60, 370, secondLilies, 0.5);
        }


        liliesGrowing(firstLilies);

        Timeline timeline1 = new Timeline(
                new KeyFrame(Duration.seconds(3), event -> {
                    liliesGrowing(secondLilies);
                })
        );
        timeline1.setCycleCount(1); // Запускать только один раз
        timeline1.play();

        //lilies.add(new LilyImageView(lily, 0,0, 0, 0, 600, 400));
    }

    private boolean checkCrossingFrogAndLilies() {
        for (LilyImageView lily: lilies) {
            Line line = new Line(frog.getLayoutX(), frog.getLayoutY() + 30, frog.getLayoutX() + 30, frog.getLayoutY() + 30);
            if (line.getBoundsInParent().intersects(lily.getLily().getBoundsInParent())) return false;
        }
        return true;
    }

    private void createNewFlower(int x, int y, int startX, int startY, int endX, int endY, ArrayList<LilyImageView> groupLilies, double size) {
        ImageView flower = new ImageView();
        flower.setImage(image1);
        flower.setFitHeight(size);
        flower.setFitWidth(size);
        flower.setLayoutX(x);
        flower.setLayoutY(y);
        LilyImageView newLily = new LilyImageView(flower, x, y, startX, startY, endX, endY);
        mainPane.getChildren().add(flower);
        groupLilies.add(newLily);
        lilies.add(newLily);
    }

    private void liliesGrowing(ArrayList<LilyImageView> lilies) {
        AtomicBoolean isGrowing = new AtomicBoolean(false);
        Timeline timeline1 = new Timeline();
        timeline1.setCycleCount(Timeline.INDEFINITE); // Один цикл на всю анимацию

        KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.1), event -> {
            // Обрабатываем все цветы в одной анимации
            if ((lilies.get(0).getLily().getFitWidth() > 1 || lilies.get(0).getLily().getFitHeight() > 1) && !isGrowing.get()) {
                // Уменьшаем размер всех цветов
                for (LilyImageView lilyPicture : lilies) {
                    lilyPicture.getLily().setFitWidth(lilyPicture.getLily().getFitWidth() - 0.5);
                    lilyPicture.getLily().setFitHeight(lilyPicture.getLily().getFitHeight() - 0.5);
                }
            }
            else if (lilies.get(0).getLily().getFitWidth() < 30 && lilies.get(0).getLily().getFitHeight() < 30) {
                isGrowing.set(true);
                // Увеличиваем размер всех цветов
                for (LilyImageView lilyPicture : lilies) {
                    lilyPicture.getLily().setFitWidth(lilyPicture.getLily().getFitWidth() + 0.5);
                    lilyPicture.getLily().setFitHeight(lilyPicture.getLily().getFitHeight() + 0.5);
                }
                // Останавливаем увеличение, если достигнут максимальный размер
                if (lilies.get(0).getLily().getFitWidth() == 30 || lilies.get(0).getLily().getFitHeight() == 30)
                    isGrowing.set(false);
            }

            if (lilies.get(0).getLily().getFitHeight() * lilies.get(0).getLily().getFitWidth() == 1) {
                isGrowing.set(true);
                // Перемещаем все цветы в случайные места
                for (LilyImageView lilyPicture : lilies) {
                    int randomX = random.nextInt(lilyPicture.getEndX() - lilyPicture.getStartX() + 1) + lilyPicture.getStartX();
                    lilyPicture.getLily().setLayoutX(randomX);
                    int randomY = random.nextInt(lilyPicture.getEndY() - lilyPicture.getStartY() + 1) + lilyPicture.getStartY();
                    lilyPicture.getLily().setLayoutY(randomY);
                }
            }
        });

        timeline1.getKeyFrames().add(keyFrame1);
        timeline1.play();
    }


    public void handleKeyPress(KeyEvent keyEvent) {
        System.out.println("Key Pressed: " + keyEvent.getCode()); // Печатаем нажатую клавишу

        if (keyEvent.getCode() == UP) {
            boolean isJumpPossible = false;
            for (LilyImageView flower: lilies) {
                if (frog.getBoundsInParent().intersects(flower.getLily().getBoundsInParent())) {
                    isJumpPossible = true;
                    doubleJump.set(1);
                }
            }
            if (isJumpPossible || doubleJump.get() == 2) {
                if (doubleJump.get() == 2) doubleJump.set(0);
                doubleJump.set(doubleJump.get() + 1);
                Timeline timeline = new Timeline();
                timeline.setCycleCount(300);

                KeyFrame keyFrame = new KeyFrame(Duration.seconds(0.00125), event -> {
                    isJumpDone = false;
                    frog.setLayoutY(frog.getLayoutY() - 0.5);
                    if (timeline.getCycleCount() == 300) isJumpDone = true;

                });
                timeline.getKeyFrames().add(keyFrame);
                timeline.play();
            }
        }
        if (keyEvent.getCode() == LEFT && !moveLeft) {
            moveLeft = true;
            startMoveLeft();
        }

        // Обработка движения вправо
        if (keyEvent.getCode() == RIGHT && !moveRight) {
            moveRight = true;
            startMoveRight();
        }
    }

    public void handleKeyReleased(KeyEvent keyEvent) {
        System.out.println("Key Released: " + keyEvent.getCode()); // Печатаем отпущенную клавишу

        // Останавливаем движение при отпускании клавиши
        if (keyEvent.getCode() == LEFT) {
            moveLeft = false;
            if (leftTimeline != null) {
                leftTimeline.stop();
            }
        }

        if (keyEvent.getCode() == RIGHT) {
            moveRight = false;
            if (rightTimeline != null) {
                rightTimeline.stop();
            }
        }
    }

    // Функция для начала движения влево
    private void startMoveLeft() {
        if (leftTimeline != null && leftTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Если движение уже идет, не создаем новый Timeline
        }

        leftTimeline = new Timeline(
                new KeyFrame(Duration.millis(5), e -> {
                    if (moveLeft) {
                        frog.setLayoutX(frog.getLayoutX() - 0.5); // плавное движение влево
                    }
                })
        );
        leftTimeline.setCycleCount(Timeline.INDEFINITE); // зацикливаем Timeline
        leftTimeline.play(); // запускаем движение
    }

    // Функция для начала движения вправо
    private void startMoveRight() {
        if (rightTimeline != null && rightTimeline.getStatus() == Timeline.Status.RUNNING) {
            return; // Если движение уже идет, не создаем новый Timeline
        }

        rightTimeline = new Timeline(
                new KeyFrame(Duration.millis(5), e -> {
                    if (moveRight) {
                        frog.setLayoutX(frog.getLayoutX() + 0.5); // плавное движение вправо
                    }
                })
        );
        rightTimeline.setCycleCount(Timeline.INDEFINITE); // зацикливаем Timeline
        rightTimeline.play(); // запускаем движение
    }

}
