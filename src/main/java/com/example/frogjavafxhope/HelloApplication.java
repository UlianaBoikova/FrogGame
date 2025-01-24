package com.example.frogjavafxhope;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


    public class HelloApplication extends Application {
        public final static int WIDTH = 600;
        public final static int HEIGHT = 400;

        @Override
        public void start(Stage stage) throws IOException {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/example/frogjavafxhope/beginning-hope.fxml")));
            stage.setTitle("Hello");
            stage.setScene(new Scene(root, WIDTH, HEIGHT));
            stage.show();
        }

        public static void main(String[] args) {
            launch();
        }
    }
