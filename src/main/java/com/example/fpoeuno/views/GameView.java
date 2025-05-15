package com.example.fpoeuno.views;

import com.example.fpoeuno.controllers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Stage {

    private final GameController gameController;

    public GameView() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/fpoeuno/ui/uno-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.gameController = fxmlLoader.getController();
        this.setTitle("UNO - GAME");
        this.setScene(scene);
        this.setResizable(false);
    }

    public GameController getController() { return gameController; }

    public static GameView getInstance() throws IOException {
        if (GameViewHolder.INSTANCE == null) {
            GameViewHolder.INSTANCE = new GameView();
        }
        return GameViewHolder.INSTANCE;
    }

    private static class GameViewHolder {
        private static GameView INSTANCE;
    }

}