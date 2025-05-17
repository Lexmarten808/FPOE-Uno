package com.example.fpoeuno.views;

import com.example.fpoeuno.Main;
import com.example.fpoeuno.controllers.GameController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the game window (stage) of the UNO application.
 * This class follows the singleton patter to maintain a single instance of the game view.
 */
public class GameView extends Stage {

    // Controller associated with this game view, responsible for handling game logic and UI events
    private final GameController gameController;

    /**
     * Constructs the GameView by loading the FXML layout, initializing the controller,
     * setting the scene, window title, disabling resizing, and setting the application icon.
     *
     * @throws IOException if the FXML resource cannot be loaded.
     */
    public GameView() throws IOException {
        // Load the game.fxml file and create the scene
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("game.fxml")
        );
        Scene scene = new Scene(fxmlLoader.load());

        // Retrieve the controller instance from the FXMLLoader
        this.gameController = fxmlLoader.getController();

        // Set up the stage properties
        this.setTitle("UNO - GAME");
        this.setScene(scene);
        this.setResizable(false);

        // Set the window icon from resources
        this.getIcons().add(new Image(Main.class.getResourceAsStream("images/icons/uno-logo.png")));
    }

    /**
     * Returns the controller instance associated with this game view.
     *
     * @return the GameController instance.
     */
    public GameController getController() { return gameController; }

    /**
     * Provides a singleton instance of the GameView.
     * If the instance does not existe, it creates a new one.
     *
     * @return the single instance of GameView.
     * @throws IOException if the FXML resource cannot be loaded.
     */
    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }
        return GameViewHolder.INSTANCE;
    }

    /**
     * Inner static class responsible for holding the singleton instance of GameView.
     */
    private static class GameViewHolder {
        private static GameView INSTANCE;
    }

}