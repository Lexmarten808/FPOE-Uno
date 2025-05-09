package com.example.fpoeuno.models;

/**
 * Represents a single UNO card with its image URL, type, color, and number.
 * This model encapsulates all relevant data used throughout the game.
 */
public class Card {

    private String imageUrl;
    private String type;
    private String color;
    private byte number;

    /**
     * Constructs a new UNO card.
     *
     * @param imageUrl the path or URL to the card's image
     * @param type     the card's type ("number", "skip", "+2", "+4")
     * @param color    the color of the card ("blue", "green", "red", "yellow", "wild")
     * @param number   the card's number (0–9 for number cards, -1 for special cards)
     */
    public Card(String imageUrl, String type, String color, byte number) {
        this.imageUrl = imageUrl;
        this.type = type;
        this.color = color;
        this.number = number;
    }

    /**
     * Returns a string representation of the card for console display.
     *
     * @return formatted string of card attributes
     */
    @Override
    public String toString() {
        String numberDisplay = (number >= 0) ? String.valueOf(number) : "N/A";
        return String.format("[%s | %s | %s]", type, color, numberDisplay);
    }

    /** @return the image path or URL of the card */
    public String getImageUrl() { return imageUrl; }

    /** @param imageUrl sets the image path or URL of the card */
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    /** @return the type of the card */
    public String getType() { return type; }

    /** @param type sets the type of the card */
    public void setType(String type) { this.type = type; }

    /** @return the color of the card */
    public String getColor() { return color; }

    /** @param color sets the color of the card */
    public void setColor(String color) { this.color = color; }

    /** @return the card's number (or -1 for non-numeric cards) */
    public byte getNumber() { return number; }

    /** @param number sets the card's number */
    public void setNumber(byte number) { this.number = number; }

}