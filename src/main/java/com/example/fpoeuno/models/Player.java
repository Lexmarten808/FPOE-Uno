package com.example.fpoeuno.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a UNO player, either humar or machine, with a hand of cards.
 * Handles card drawing, playing, and logic for playable cards.
 */
public class Player {

    private final String name;
    private final boolean isHuman;
    private final List<Card> hand;

    /**
     * Constructs a new Player instance.
     *
     * @param name    the name of the player
     * @param isHuman true if the player is a human; false if machine
     */
    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void drawCard(Card card) {
        hand.add(card);
    }

    /**
     * Removes a card from the player's hand.
     *
     * @param card the card to remove
     */
    public void playCard(Card card) {
        hand.remove(card);
    }

    /**
     * Checks if the player has at least one card that can be played.
     *
     * @param topCard the card currently on top of the discard pile
     * @return true if a playable card exists in hand; false otherwise
     */
    public boolean hasPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (canPlayCard(card, topCard)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if a specific card is playable on top of the given card.
     *
     * @param card    the card from the player's hand
     * @param topCard the top card on the discard pile
     * @return true if the card can be legally played, false otherwise
     */
    private boolean canPlayCard(Card card, Card topCard) {
        return card.getColor().equals(topCard.getColor()) ||
                card.getNumber() == topCard.getNumber() ||
                card.getType().equals(topCard.getType()) ||
                card.getType().equals("wild") ||
                card.getType().equals("+4");
    }

    /**
     * Returns the player's hand (list of cards).
     *
     * @return the current hand of the player
     */
    public List<Card> getHand() { return hand; }

    /** @return the player's name */
    public String getName() { return name; }

    /** @return true if the player is human; false if machine */
    public boolean isHuman() { return isHuman; }

    /**
     * Prints the player's hand to the console.
     * Useful for debugging.
     */
    public void printHand() {
        System.out.println(name + " has " + hand.size() + " card(s):");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + hand.get(i));
        }
    }

}