package com.example.lab6;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PrimeMultiples extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PrimeMultiples.class.getResource("primeMultiple.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 470);
        stage.setTitle("Prime multiples");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}