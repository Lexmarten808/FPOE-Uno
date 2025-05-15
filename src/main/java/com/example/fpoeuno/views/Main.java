package com.example.fpoeuno.views;

import com.example.fpoeuno.models.Game;
import com.example.fpoeuno.models.SoundManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Entry point for the UNO game application.
 * This class initializes the primary stage with the main menu UI and starts background music.
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        MenuView menuView = MenuView.getInstance();
        menuView.show();
    }

    public static void main(String[] args) { launch(); }

}