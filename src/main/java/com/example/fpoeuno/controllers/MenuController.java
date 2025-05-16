package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.*;
import com.example.fpoeuno.views.GameView;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    private TextField textFieldNickname;

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
            human.addCard(deck.drawCard());
            computer.addCard(deck.drawCard());
        }

        Card topCard = deck.drawCard();
        deck.discardCard(topCard);


        GameView gameView = GameView.getInstance();

        gameView.show();

        gameView.getController().setHuman(human);
        gameView.getController().setComputer(computer);
        gameView.getController().setDeck(deck);
        gameView.getController().setTopCard(topCard);
        gameView.getController().firstCardLogic(topCard);//logica de la primera carta

        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    @FXML
    void onActionButtonAyuda(ActionEvent event) {
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

    @FXML
    void onActionButtonSound(ActionEvent event) { SoundManager.toggleMusic("music.mp3"); }

}