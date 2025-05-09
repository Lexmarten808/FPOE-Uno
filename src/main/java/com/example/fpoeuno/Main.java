package com.example.fpoeuno;

import com.example.fpoeuno.views.MenuStage;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MenuStage.getInstance();
    }

    public static void main(String[] args) {
        launch();
    }

}