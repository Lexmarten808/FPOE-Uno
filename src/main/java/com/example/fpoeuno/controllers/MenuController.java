package com.example.fpoeuno.controllers;
import com.example.fpoeuno.models.AlertHelper;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;

public class MenuController {

    @FXML
    void onActionMouseClicked(MouseEvent event) {

        AlertHelper.showInfoAlert("Reglas","Reglas de UNO","- Cada jugador comienza con 12 cartas.\n" +
                "- La partida inicia con una carta aleatoria en la mesa.\n" +
                "- Juega una carta que coincida en color o número.\n" +
                "- Los comodines pueden jugarse en cualquier momento.\n" +
                "- Presiona el botón UNO antes de jugar tu última carta.\n" +
                "- Si no lo haces, tendrás 2–3 segundos o serás penalizado con una carta.");

    }

}
