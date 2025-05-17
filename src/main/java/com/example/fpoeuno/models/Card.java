package com.example.fpoeuno.models;

/**
 * Represents a playing card in the UNNO game.
 * Each card has a color, a value, and an associated image path.
 */
public class Card {

    private String color;
    private final String value;
    private final String imagePath;

    /**
     * Constructs a Card with the specified color, value, and image path.
     *
     * @param color     the color of the card.
     * @param value     the value or type of the card.
     * @param imagePath the path to the image representing the card.
     */
    public Card(String color, String value, String imagePath) {
        this.color = color;
        this.value = value;
        this.imagePath = imagePath;
    }

    /**
     * Sets a new color for the card.
     *
     * @param color the new color to assign to the card.
     * @return the updated color of the card.
     */
    public String setColor(String color) { this.color = color; return color; }

    /**
     * Returns the current color of the card.
     *
     * @return the card color.
     */
    public String getColor() { return color; }

    /**
     * Returns the value of the card.
     *
     * @return the card value.
     */
    public String getValue() { return value; }

    /**
     * Returns the image path representing the card.
     *
     * @return the image path string.
     */
    public String getImagePath() { return imagePath; }

    /**
     * Returns a string representation of the card.
     *
     * @return a string containing the card's color, value, and image path.
     */
    @Override
    public String toString() { return "Card{" + color + ", " + value + ", " + imagePath + "}"; }

}