package com.example.fpoeuno.controllers;

import com.example.fpoeuno.models.SoundManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * Controller for the main game view.
 * Manages user interactions related to game control elements, such as sound.
 */
public class GameController {

    /**
     * Handles the sound toggle button click.
     * Plays or stops the background music depending on its current state.
     *
     * @param event the ActionEvent triggered by the button click
     */
    @FXML
    void onActionButtonSound(ActionEvent event) {
        SoundManager.toggleMusic("music.mp3");
    }

}