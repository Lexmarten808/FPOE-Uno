package com.example.fpoeuno.controllers;
import com.example.fpoeuno.models.AlertHelper;
import com.example.fpoeuno.models.SoundManager;
import com.example.fpoeuno.views.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {

    @FXML
    void onActionMouseClickedQuestionMark(MouseEvent event) {

        AlertHelper.showInfoAlert("Reglas","Reglas de UNO","-1. Cada jugador comienza con 5 cartas.\n" +
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
    @FXML
    void OnActionIniciarButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("/com/example/fpoeuno/ui/uno-game.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setTitle("UNO Game");
            stage.setScene(scene);
            stage.show();

            ((Stage) ((Node) event.getSource()).getScene().getWindow()).close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
