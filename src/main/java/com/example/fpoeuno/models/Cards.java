package com.example.fpoeuno.models;

/**
 * Represents a UNO card with its image URL, type, color, and number.
 * This model is used to manage card data within the game.
 */
public class Cards {

    private String cardImageUrl;
    private String cardType;
    private String cardColor;
    private byte cardNumber;

    /**
     * Constructs a new card with the specified attributes.
     *
     * @param cardImageUrl the URL or path to the card's image
     * @param cardType     the type of the card (e.g., "number", "skip", "reverse")
     * @param cardColor    the color of the card (e.g., "red", "blue", "wild")
     * @param cardNumber   the number associated with the card (if applicable)
     */
    public Cards(String cardImageUrl, String cardType, String cardColor, byte cardNumber) {
        this.cardImageUrl = cardImageUrl;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cardNumber = cardNumber;}

    /** @return the URL or path to the card's image */
    public String getCardImageUrl() { return cardImageUrl; }

    /** @return the type of the card */
    public String getCardType() { return cardType; }

    /** @return the color of the card */
    public String getCardColor() { return cardColor; }

    /** @return the number associated with the card */
    public byte getCardNumber() { return cardNumber; }

    /** @param cardImageUrl sets the URL or path to the card's image */
    public void setCardImageUrl(String cardImageUrl) { this.cardImageUrl = cardImageUrl; }

    /** @param cardType sets the type of the card */
    public void setCardType(String cardType) { this.cardType = cardType; }

    /** @param cardColor sets the color of the card */
    public void setCardColor(String cardColor) { this.cardColor = cardColor; }

    /** @param cardNumber sets the number associated with the card */
    public void setCardNumber(byte cardNumber) { this.cardNumber = cardNumber; }

}