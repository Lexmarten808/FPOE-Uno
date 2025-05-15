package com.example.fpoeuno.views;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuView extends Stage {

    public MenuView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/fpoeuno/ui/uno-menu.fxml"));
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root);
        this.setTitle("UNO - MENU");
        this.setScene(scene);
        this.setResizable(false);
    }

    public static MenuView getInstance() throws IOException {
        if (MenuViewHolder.INSTANCE == null) {
            MenuViewHolder.INSTANCE = new MenuView();
        }
        return MenuViewHolder.INSTANCE;
    }

    private static class MenuViewHolder {
        private static MenuView INSTANCE;
    }

}
