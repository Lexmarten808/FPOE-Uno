package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.Card;
import com.example.fpoeuno.models.Game;
import com.example.fpoeuno.models.Player;
import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;
import java.util.Objects;

/**
 * Controller class for the main game view.
 * This class manages user interactions related to the game, including
 * displaying the player's name, handling sound toggle actions, and initializing the game.
 */
public class GameController {

    @FXML
    private ImageView topCard;

    @FXML
    private Pane playerHandPane;

    @FXML
    private Pane machineHandPane;

    @FXML
    private ImageView topCardImageView;

    @FXML
    private Label humanName;

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

    private Player humanPlayer;

    /**
     * Sets the player for the game and updates the player's name on the interface.
     *
     * @param humanPlayer the Player object representing the current player.
     */
    public void setPlayer(Player humanPlayer) {
        this.humanPlayer = humanPlayer;
        if (humanName != null) {
            humanName.setText(humanPlayer.getName());
        } else {
            humanName.setText("Jugador");
        }
    }

    /**
     * Initializes the player's hand view by updating the layout with the player's cards,
     * setting up the images, and adjusting their position within the game interface.
     *
     * @param hand a list of Card objects representing the player's current hand.
     */
    public void showPlayerHand(List<Card> hand) {
        // Clear the current hand before updating
        playerHandPane.getChildren().clear();

        double xOffset = 0; // X-axis offset initializes to 0

        // Iterate over the cards in the player's hand
        for (Card card : hand) {
            // Load the card's image
            Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/fpoeuno/" + card.getImageUrl())).toExternalForm());
            ImageView imageView = new ImageView(image);

            // Adjust the size of the card
            imageView.setFitHeight(90);
            imageView.setFitWidth(58);
            imageView.setPreserveRatio(true);

            imageView.setCursor(Cursor.HAND); // Change the cursor to a hand when hovering the card
            imageView.setLayoutX(xOffset); // Set the card's X position in the Pane
            imageView.setLayoutY(0); // Set the card's Y position

            playerHandPane.getChildren().add(imageView); // Add the card to the Pane

            xOffset += 63; // Increase de X-axis offset for the next card
        }
    }

    public void showMachineHand(List<Card> hand) {
        machineHandPane.getChildren().clear();

        double cardSpacing = 63; // Espacio entre cartas
        double cardWidth = 58;   // Ancho de cada carta
        double targetX = 685;    // Posición X deseada para la última carta
        double targetY = 55;     // Posición Y deseada

        double startX = targetX - (hand.size() - 1) * cardSpacing;

        Image image = new Image(Objects.requireNonNull(getClass().getResource("/com/example/fpoeuno/images/cards-uno/card_uno.png")).toExternalForm());

        for (int i = 0; i < hand.size(); i++) {
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(90);
            imageView.setFitWidth(cardWidth);
            imageView.setPreserveRatio(true);
            imageView.setCursor(Cursor.HAND);

            double x = startX + i * cardSpacing;
            imageView.setLayoutX(x);
            imageView.setLayoutY(targetY);

            machineHandPane.getChildren().add(imageView);
        }
    }

    /**
     * Sets the name of the player on the user interface.
     */
    public void setPlayerName(String name) {
        humanName.setText(humanPlayer.getName());
    }

    public void initialize() {
        Game game = new Game();

        //declaration of a Card objet named fisrtCard that contains the first card in the table
        Card firstCard = game.getDeck().getTopDiscard();

        /*if the firstCard is not null and the topCart either
        the image is loaded in the image view labeled as "topCard"
        if (firstCard != null && topCard != null) {
            String imagePath = "/com/example/fpoeuno/" + firstCard.getImageUrl(); // Asegúrate que esto devuelva algo como "images/cards-uno/1_red.png"
            Image image = new Image(getClass().getResource(imagePath).toExternalForm());
            topCard.setImage(image);
        }
        */

        // Mostrar la mano del humano
        showPlayerHand(game.getHuman().getHand());
        showMachineHand(game.getMachine().getHand());

        game.printGameInfo();
    }

}