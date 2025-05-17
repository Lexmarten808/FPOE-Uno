package com.example.fpoeuno;

import com.example.fpoeuno.models.SoundManager;
import com.example.fpoeuno.views.MenuView;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class that serves as the entry point for the UNO JavaFX application.
 * It extends the JavaFX Application class to set up and start the application.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by initializing and displaying the main menu,
     * and toggling background music playback.
     *
     * @param stage the primary stage for this application, provided by the JavaFX runtime.
     * @throws IOException if loading the MenuView instance fails.
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Start or toggle background music
        SoundManager.toggleMusic("music.mp3");

        // Obtain the singleton instance of the MenuView and display it
        MenuView menuView = MenuView.getInstance();
        menuView.show();
    }

    /**
     * Main method, launcehs the JavaFX application.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) { launch(); }

}