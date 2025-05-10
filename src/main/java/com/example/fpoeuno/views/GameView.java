package com.example.fpoeuno.views;

import com.example.fpoeuno.controllers.GameController;
import com.example.fpoeuno.models.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class GameView extends Stage {

    private final GameController gameController;

    public GameView() throws IOException {
        // Load the main game interface from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/fpoeuno/uno-game.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        // Get the controller instance
        this.gameController = fxmlLoader.getController();

        // Crear instancia única del modelo Game
        Game game = new Game();
        gameController.setGame(game); // Inyectar el modelo al controlador

        // Establecer atributos visuales
        getIcons().add(new Image(getClass().getResourceAsStream("/com/example/fpoeuno/images/icons/uno-logo.png")));
        setTitle("UNO");
        setScene(scene);
        setResizable(false);
        show();
    }

    public GameController getGameController() { return gameController; }

    public static GameView getInstance() throws IOException {
        if (GameStageHolder.INSTANCE == null) {
            return GameStageHolder.INSTANCE = new GameView();
        } else {
            return GameStageHolder.INSTANCE;
        }
    }

    private static class GameStageHolder {
        private static GameView INSTANCE;
    }

}