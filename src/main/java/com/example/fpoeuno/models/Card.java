package com.example.fpoeuno.models;

import com.example.fpoeuno.models.enums.CardColor;
import com.example.fpoeuno.models.enums.CardType;

public class Card {

    private final CardType type;
    private CardColor color;
    private final int value;
    private final String imageUrl;
    private CardColor chosenColor;

    public Card(CardType type, CardColor color, int value, String imageUrl) {
        this.type = type;
        this.color = color;
        this.value = value;
        this.imageUrl = imageUrl;
        this.chosenColor = null; // Por defecto no hay color elegido para los comodines
    }

    // --- Getters ---
    public CardType getType() { return type; }
    public CardColor getColor() { return color; }
    public int getValue() { return value; }
    public String getImageUrl() { return imageUrl; }

    // --- Setters ---
    public void setColor(CardColor color) {
        if (this.color == CardColor.WILD && (this.type == CardType.WILD || this.type == CardType.WILD_DRAW_FOUR)) {
            this.color = color;
        } else {
            throw new UnsupportedOperationException("Only wild cards can have their color changed.");
        }
    }

    // --- Chosen Color Logic for Wild Cards ---
    public void setChosenColor(CardColor chosenColor) {
        if (this.type == CardType.WILD || this.type == CardType.WILD_DRAW_FOUR) {
            this.chosenColor = chosenColor;
        } else {
            throw new UnsupportedOperationException("Only wild cards can set a chosen color.");
        }
    }

    public CardColor getEffectiveColor() {
        // Si la carta es comodín, se devuelve el color elegido, si no, se devuelve el color original.
        return this.type == CardType.WILD || this.type == CardType.WILD_DRAW_FOUR ?
                (chosenColor != null ? chosenColor : color) : color;
    }

    // --- Can Play Logic ---
    public boolean canPlayOn(Card other) {
        if (this.getEffectiveColor() == CardColor.WILD) return true;

        return this.getEffectiveColor() == other.getColor() ||
                this.type == other.type ||
                (this.type == CardType.NUMBER && other.type == CardType.NUMBER && this.value == other.value);
    }

    @Override
    public String toString() {
        if (type == CardType.NUMBER) {
            return String.format("[%s | %d]", color, value);
        } else {
            return String.format("[%s | %s]", color, type);
        }
    }

}