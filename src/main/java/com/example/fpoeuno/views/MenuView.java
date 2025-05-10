package com.example.fpoeuno.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView extends Stage {

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

    public static MenuView getInstance() throws IOException {
        if (MenuStageHolder.INSTANCE == null) {
            return MenuStageHolder.INSTANCE = new MenuView();
        } else {
            return MenuStageHolder.INSTANCE;
        }
    }

    private static class MenuStageHolder {
        private static MenuView INSTANCE;
    }

}