package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class GameController {

    @FXML
    void OnActionSoundButton(ActionEvent event) {
        SoundManager.toggleMusic("musica.mp3");

    }

}
