package com.example.fpoeuno.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Class representing the main menu window of the UNO game.
 * This class extends {@link javafx.stage.Stage} and is responsible for loading the main menu interface from an FXML file,
 * setting the scene, the application icon, and controlling background music.
 */
public class MenuView extends Stage {

    /**
     * Constructor that loads the main menu interface and sets up the menu window.
     * It also initializes the background music.
     *
     * @throws IOException if an error occurs while loading the FXML file or the icon image.
     */
    public MenuView() throws IOException {
        // Load the main menu interface from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "/com/example/fpoeuno/uno-menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Set the application icon
        getIcons().add(new Image(getClass().getResourceAsStream("/com/example/fpoeuno/images/icons/uno-logo.png")));

        // Toggle background music
        // SoundManager.toggleMusic("music.mp3");

        // Set the title, scene, and show the window
        setTitle("UNO");
        setScene(scene);
        setResizable(false);
        show();
    }

    /**
     * Static method implementing the Singleton pattern to ensure there is only one instance of {@link MenuView}.
     *
     * @return the unique instance of {@link MenuView}.
     * @throws IOException if an error occurs while creating the instance.
     */
    public static MenuView getInstance() throws IOException {
        if (MenuStageHolder.INSTANCE == null) {
            return MenuStageHolder.INSTANCE = new MenuView();
        } else {
            return MenuStageHolder.INSTANCE;
        }
    }

    /**
     * Helper static class that holds the unique instance of {@link MenuView}.
     * This is used to efficiently implement the Singleton pattern.
     */
    private static class MenuStageHolder {
        private static MenuView INSTANCE;
    }

}