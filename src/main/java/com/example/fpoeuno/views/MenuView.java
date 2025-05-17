package com.example.fpoeuno.views;

import com.example.fpoeuno.Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Represents the main menu window (stage) of the UNO application.
 * This class is implemented as a singleton to ensure only one instance of the menu is created.
 */
public class MenuView extends Stage {

    /**
     * Constructs the MenuView by loading the FXML layout, setting the scene,
     * window title, disabling resizing, and setting the application icon.
     *
     * @throws IOException if the FXML resource cannot be loaded.
     */
    public MenuView() throws IOException {
        // Load the FXML file for the menu layout
        FXMLLoader fxmlLoader = new FXMLLoader(
                Main.class.getResource("menu.fxml")
        );
        Parent root = fxmlLoader.load();

        // Create and set the scene
        Scene scene = new Scene(root);
        this.setTitle("UNO - MENU");
        this.setScene(scene);
        this.setResizable(false);

        // Set the window icon from resources
        this.getIcons().add(new Image(Main.class.getResourceAsStream("images/icons/uno-logo.png")));
    }

    /**
     * Provides a singleton instance of the MenuView.
     * If the instance does not exist, it creates a new one.
     *
     * @return the single instance of MenuView.
     * @throws IOException if the FXML resource cannot be loaded.
     */
    public static MenuView getInstance() throws IOException {
        if (MenuViewHolder.INSTANCE == null) {
            MenuViewHolder.INSTANCE = new MenuView();
        }
        return MenuViewHolder.INSTANCE;
    }

    /**
     * Inner static class responsible for holding the singleton instance of MenuView.
     */
    private static class MenuViewHolder {
        private static MenuView INSTANCE;
    }

}