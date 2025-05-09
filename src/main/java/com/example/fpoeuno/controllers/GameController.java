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
 * Controller class for the main game view.
 * This class manages user interactions related to the game, including
 * displaying the player's name, handling sound toggle actions, and initializing the game.
 */
public class GameController {

    private Player humanPlayer;

    /**
     * Label that displays the player's name.
     */
    @FXML
    private Label humanName;

    /**
     * Sets the player for the game and updates the player's name on the interface.
     *
     * @param humanPlayer the Player object representing the current player.
     */
    public void setPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
        if (humanName != null) {
            humanName.setText(humanPlayer.getName());
        }
    }

    /**
     * HBox container that holds the player's hand of cards.
     */
    @FXML
    private HBox playerHandBox;

    /**
     * ImageView that displays the top card in the center of the screen.
     */
    @FXML
    private ImageView topCardImageView;

    private Game game;

    /**
     * Initializes the game view by setting up the game instance, adjusting the layput,
     * and starting the game.
     */
    public void initialize() {
        // This runs when the view is loaded
        game = new Game(); // Initialize the game instance

        // Set spacing between cards in the player's hand
        playerHandBox.setSpacing(5); // Set spacing to 5px

        // Mostrar la mano inicial
        // showPlayerHand(game.getHuman().getHand());

        // Start the game and display the first card
        game.startGame(); // This will update the discard pile

        game.getDeck().printDrawPile();
        game.getDeck().printDiscardPile();
    }

    /*
    public void showPlayerHand(List<Card> hand) {
        playerHandBox.getChildren().clear(); // Limpiar antes de actualizar

        double xOffset = 0;  // Desplazamiento en el eje X, inicializado a 0

        for (Card card : hand) {
            String imagePath = "/com/example/fpoeuno/" + card.getImageUrl();
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(90); // Ajusta el tamaño si quieres
            imageView.setFitWidth(58); // Ajusta el tamaño si quieres
            imageView.setPreserveRatio(true);

            // Establecer la posición X de la carta
            imageView.setLayoutX(xOffset);

            // Agregar la carta a la mano
            playerHandBox.getChildren().add(imageView);

            // Incrementar el desplazamiento para la siguiente carta
            xOffset += 100;  // Aumentamos el desplazamiento en 50px
        }
    }

     */

    /**
     * Sets the name of the player on the user interface.
     */
    public void setPlayerName(String name) {
        humanName.setText(humanPlayer.getName());
    }

    /**
     * Handles the sound toggle button click event.
     * It toggles background music on or off based on its current state.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        // Toggle background music
        SoundManager.toggleMusic("music.mp3");
    }

}