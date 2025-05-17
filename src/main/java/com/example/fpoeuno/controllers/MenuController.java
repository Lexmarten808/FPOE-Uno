package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;
import com.example.fpoeuno.views.GameView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main menu screen.
 */
public class MenuController {

    @FXML private TextField textFieldNickname;

    /**
     * Handles the Iniciar button click.
     *
     * @param event the button click event.
     * @throws IOException if the game view fails to load.
     */
    @FXML
    void onActionButtonIniciar(ActionEvent event)throws IOException {
        String nickname = textFieldNickname.getText();
        if (nickname.isBlank()) {
            nickname = "Jugador"; // Default nickname
        }

        // Create and configure human player
        Player human = new Player("Jugador", true);
        human.setNickname(nickname);

        // Create deck and computer player
        Deck deck = new Deck();
        Player computer = new Player("Computer", false);

        // Deal 5 cards to each player
        for (int i = 0; i < 5; i++) {
            human.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
        }

        // Set up the initial top card
        Card topCard = deck.drawCard();
        deck.discardCard(topCard);

        // Load the game view (singleton)
        GameView gameView = GameView.getInstance();
        gameView.show();

        // Pass game state to the controller
        gameView.getController().setHuman(human);
        gameView.getController().setComputer(computer);
        gameView.getController().setDeck(deck);
        gameView.getController().setTopCard(topCard);
        gameView.getController().firstCardLogic(topCard); // Apply logic for the first card

        // Close the current menu window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Displays an alert with a summary of UNO rules.
     * Triggered when the user clicks the "Ayuda" button.
     *
     * @param event the button click event.
     */
    @FXML
    void onActionButtonAyuda(ActionEvent event) {
        AlertHelper.showInfoAlert(
                "Reglas",
                "Reglas de UNO",
                "-1. Cada jugador comienza con 5 cartas.\n" +
                        "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                        "-3. Juega una carta que coincida en color o número.\n" +
                        "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                        "-5. Presiona el botón UNO para jugar tu última carta, tendrás 2-4 segundos.\n" +
                        "-6. Si no lo haces, serás penalizado con una carta."
        );
    }

    /**
     * Toggles background music when the sound button is clicked.
     *
     * @param event the button click event.
     */
    @FXML
    void onActionButtonSound(ActionEvent event) { SoundManager.toggleMusic("music.mp3"); }

}