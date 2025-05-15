package com.example.fpoeuno.models;

public class Game {

    private String currentTurn = "Human";
    private String establishedColor;
    private String establishedValue;

    public Game() {}

    public String getCurrentTurn() { return currentTurn; }
    public String getEstablishedColor() { return establishedColor; }
    public String getEstablishedValue() { return establishedValue; }

    public void setCurrentTurn(String currentTurn) { this.currentTurn = currentTurn; }
    public void setEstablishedColor(String establishedColor) { this.establishedColor = establishedColor; }
    public void setEstablishedValue(String establishedValue) { this.establishedValue = establishedValue; }

}