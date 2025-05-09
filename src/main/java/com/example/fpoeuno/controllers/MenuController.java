package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.alerts.AlertInformation;
import com.example.fpoeuno.models.alerts.IAlert;
import com.example.fpoeuno.models.Player;
import com.example.fpoeuno.models.SoundManager;
import com.example.fpoeuno.views.GameStage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for managing the main menu screen of the UNO game.
 * This class handles interactions such as starting the game, toggling sound,
 * and showing game rules when requested.
 */
public class MenuController {

    /**
     * TextField for the player to input their nickname.
     */
    @FXML
    private TextField nicknameTextField;

    /**
     * Starts a new game by retrieving the nickname from the text field,
     * creating a new {@link Player} instance, and transitioning to the game screen.
     * Closes the current menu window and opens the game stage.
     *
     * @param event the action event triggered by clicking the "¡A jugar!" button.
     * @throws IOException if an error occurs while opening the game stage.
     */
    @FXML
    void onActionButtonStart(ActionEvent event) throws IOException {
        String nickname = nicknameTextField.getText();
        Player humanPlayer = new Player(nickname, true);

        // Close the current window (MenuStage)
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();

        // Open the game window (GameStage)
        GameStage.getInstance().getGameController().setPlayer(humanPlayer);
    }

    /**
     * Displays an informational alert dialog with the rules of the UNO game.
     * This method is triggered when the "Ayuda" button is clicked.
     *
     * @param event the action event triggered by clicking the "Ayuda" button.
     */
    @FXML
    void onActionButtonHelp(ActionEvent event) {
        String title = "Reglas";
        String header = "Reglas de UNO";
        String content =
                "-1. Cada jugador comienza con 5 cartas.\n" +
                "-2. La partida inicia con una carta aleatoria en la mesa.\n" +
                "-3. Juega una carta que coincida en color o número.\n" +
                "-4. Los comodines pueden jugarse en cualquier momento.\n" +
                "-5. Presiona el botón UNO antes de jugar tu última carta.\n" +
                "-6. Si no lo haces, tendrás 2–3 segundos o serás penalizado con una carta.";

        // Display an informational alert with the game rules
        IAlert alert = new AlertInformation();
        alert.showAlert(Alert.AlertType.INFORMATION, title, header, content);
    }

    /**
     * Toggles the background music on or off when the sound button is clicked.
     * This method is triggered by clicking the sound button.
     *
     * @param event the action event triggered by clicking the sound button.
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        // Toggle background music
        SoundManager.toggleMusic("music.mp3");
    }

}