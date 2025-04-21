package com.example.fpoeuno.controllers;
import com.example.fpoeuno.models.AlertHelper;
import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MenuController {

    @FXML
    void onActionMouseClicked(MouseEvent event) {

        AlertHelper.showInfoAlert("Reglas","Reglas de UNO","-1. Cada jugador comienza con 12 cartas.\n" +
                "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                "-3. Juega una carta que coincida en color o número.\n" +
                "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                "-5. Presiona el botón UNO antes de jugar tu última carta.\n" +
                "-6. Si no lo haces, tendrás 2–3 segundos o serás penalizado con una carta.");

    }

    @FXML
    void onActionSoundButton(ActionEvent event) {

        SoundManager.toggleMusic("musica.mp3");

    }

}
