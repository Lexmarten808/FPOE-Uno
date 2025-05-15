package com.example.fpoeuno.models;

public class Card {

    private final String color;
    private final String value;
    private final String imagePath;

    public Card(String color, String value, String imagePath) {
        this.color = color;
        this.value = value;
        this.imagePath = imagePath;
    }

    public String getColor() { return color; }
    public String getValue() { return value; }
    public String getImagePath() { return imagePath; }

    @Override
    public String toString() { return "Card{" + color + ", " + value + ", " + imagePath + "}"; }

}