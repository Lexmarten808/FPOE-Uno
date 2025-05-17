package com.example.fpoeuno.models;

/**
 * Represents the core state of the UNO game.
 * This class tracks the current player's turn and the most recently established
 * color and value on the discard pile.
 */
public class Game {

    private String currentTurn = "Human";
    private String establishedColor;
    private String establishedValue;

    /** Default constructor for initializing a game instance. */
    public Game() {}

    // ===== Getters ======
    public String getCurrentTurn() { return currentTurn; }
    public String getEstablishedColor() { return establishedColor; }
    public String getEstablishedValue() { return establishedValue; }

    // ===== Setters =====
    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
    public void setEstablishedColor(String establishedColor) { this.establishedColor = establishedColor; }
    public void setEstablishedValue(String establishedValue) { this.establishedValue = establishedValue; }

}