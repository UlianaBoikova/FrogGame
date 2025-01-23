package com.example.frogjavafxhope;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoseController {

    @FXML
    private ImageView losePicture;

    @FXML
    private Pane mainPane;

    @FXML
    private Button startOverButton;

    @FXML
    void initialize() {
        Image image = new Image("file:///Users/ulianaboikova/IdeaProjects/FrogJavaFXHope/src/main/java/com/example/frogjavafxhope/assets/Lose.gif");
        losePicture.setImage(image);
        startOverButton.setOnAction(event -> {
            startOverButton.getScene().getWindow().hide();
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/com/example/frogjavafxhope/game-hope.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }

}
