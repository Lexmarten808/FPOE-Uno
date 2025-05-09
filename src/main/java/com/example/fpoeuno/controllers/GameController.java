package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.Card;
import com.example.fpoeuno.models.Game;
import com.example.fpoeuno.models.Player;
import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.InputStream;
import java.util.List;

/**
 * Controller for the main game view.
 * Manages user interactions related to game control elements, such as sound.
 */
public class GameController {
  Game game=new Game();

    @FXML
    private Label humanName;
    @FXML
    private HBox mazoJugador;
    @FXML
    private HBox mazoMaquina;

    public void setPlayerName(String name) {
        humanName.setText(name);}

    public void inicializarJuego() {
        game.startGame();

    }
    private void mostrarCartasJugador() {
        if (game == null) {
            System.out.println("El objeto 'game' no está inicializado.");
            return;
        }
        // Obtener las cartas del jugador

        mazoJugador.getChildren().clear();

        for (Card carta : game.getHuman().getHand()) {
            InputStream stream = getClass().getResourceAsStream(carta.getImageUrl());
            if (stream == null) {
                System.out.println("❌ No se encontró la imagen: " + carta.getImageUrl());
            } else {
                System.out.println("✅ Imagen cargada correctamente: " + carta.getImageUrl());
                Image image = new Image(stream);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
                mazoJugador.getChildren().add(imageView);
            }
        }
    }


    /**
     * Handles the sound toggle button click.
     * Plays or stops the background music depending on its current state.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");

        inicializarJuego();
        mostrarCartasJugador();

    }

}