package com.example.fpoeuno.views;

import com.example.fpoeuno.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class representing the UNO game window.
 * This class extends {@link javafx.stage.Stage} and is responsible for loading the user interface from an FXML file,
 * setting the scene, the application icon, and displaying the game window.
 */
public class GameStage extends Stage {

    /**
     * The game controller that handles the game logic and UI interactions.
     */
    private GameController gameController;

    /**
     * Constructor that loads the user interface and sets up the game window.
     *
     * @throws IOException if an error occurs while loading the FXML file or the icon image.
     */
    public GameStage() throws IOException {
        // Load the main game interface from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/example/fpoeuno/uno-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        gameController = fxmlLoader.getController();

        // Set the application icon
        getIcons().add(new Image(getClass().getResourceAsStream("/com/example/fpoeuno/images/icons/uno-logo.png")));

        // Set the title and scene, then show the window
        setTitle("UNO");
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Gets the game controller associated with this window.
     *
     * @return the game controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Static method implementing the Singleton pattern to ensure there is only one instance of {@link GameStage}.
     *
     * @return the unique instance of {@link GameStage}.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static GameStage getInstance() throws IOException {
        if (GameStageHolder.INSTANCE == null) {
            return GameStageHolder.INSTANCE = new GameStage();
        } else {
            return GameStageHolder.INSTANCE;
        }
    }

    /**
     * Helper static class that holds the unique instance of {@link GameStage}.
     * This is used to efficiently implement the Singleton pattern.
     */
    private static class GameStageHolder {
        private static GameStage INSTANCE;
    }

}