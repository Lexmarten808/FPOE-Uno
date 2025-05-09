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
public class UnoApplication extends Application {

    /**
     * Initializes the primary stage of the application.
     *
     * @param stage the primary stage provided by the JavaFX runtime
     * @throws IOException if the FXML file or image resource cannot be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        // Load the main menu interface from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(UnoApplication.class.getResource("/com/example/fpoeuno/ui/uno-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Set the application icon
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/example/fpoeuno/images/icons/uno-logo.png")));

        // Start background music
        SoundManager.toggleMusic("music.mp3");

        Game game = new Game();
        game.printHands();
        game.printDeckState();

        // Set the title and scene, then show the stage
        stage.setTitle("UNO");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Launches the JavaFX application.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        launch();
    }

}