package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;
import com.example.fpoeuno.views.GameView;
import com.example.fpoeuno.views.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller for the main menu screen of the UNO game.
 * Handles interactions such as starting the game, toggling sound, and showing game rules.
 */
public class MenuController {
    @FXML
    private TextField textFieldNickname;

    /**
     * Displays an informational alert dialog with the rules of UNO
     * when the question mark icon is clicked.
     *
     * @param event the mouse event triggered by clicking the icon
     */
    @FXML
    void onActionMouseClickedQuestionMark(MouseEvent event) {
        AlertHelper.showInfoAlert(
                "Reglas",
                "Reglas de UNO",
                "-1. Cada jugador comienza con 5 cartas.\n" +
                "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                "-3. Juega una carta que coincida en color o número.\n" +
                "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                "-5. Presiona el botón UNO antes de jugar tu última carta.\n" +
                "-6. Si no lo haces, tendrás 2–3 segundos o serás penalizado con una carta."
        );
    }

    /**
     * Toggles background music on or off when the sound button is clicked.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");
    }

    /**
     * Starts a new UNO game when the "Start" button is clicked.
     * Loads the game scene and closes the current menu window.
     *
     * @param event the action event triggered by the button click
     */
    @FXML
    void onActionButtonIniciar(ActionEvent event)throws IOException {



        String nickname = textFieldNickname.getText();
        if (nickname.isBlank()) {
            nickname = "Jugador";
        }

        Player human = new Player("Jugador", true);
        human.setNickname(nickname);

        Deck deck = new Deck();
        Player computer = new Player("Computer", false);

        for (int i = 0; i < 5; i++) {
            human.drawCard(deck.drawCard());
            computer.drawCard(deck.drawCard());
        }

        Card topCard = deck.drawCard();
        deck.discardCard(topCard);



        GameView gameView = GameView.getInstance();
        gameView.show();

        gameView.getController().setHuman(human);
        gameView.getController().setComputer(computer);
        gameView.getController().setDeck(deck);
        gameView.getController().setTopCard(topCard);

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }


}