package com.example.fpoeuno.models;

import java.util.Collections;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Represents the UNO deck as a stack of cards.
 * Handles deck creation, shuffling, drawing, discarding, and recycling cards when the draw pile is empty.
 */
public class Deck {

    private Stack<Card> drawPile;
    private Stack<Card> discardPile;

    /**
     * Constructs a new deck, initializing and shuffling the draw pile.
     */
    public Deck() {
        this.drawPile = new Stack<>();
        this.discardPile = new Stack<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Initializes the deck with standard UNO cards, including:
     * - Number cards (0-9) for each color (40)
     * - +2 cards (2 per color) (8)
     * - Skip cards (1 per color) (4)
     * - +4 wild draw cards (4)
     * - Wild color change cards (4)
     * Totally: 60 cards.
     */
    public void initializeDeck() {
        String[] colors = {"blue", "green", "red", "yellow"};
        String basePath = "images/cards-uno/";

        for (String color : colors) {
            // Add number cards 0 to 9 (1 of each per color)
            for (int i = 0; i <= 9; i++) {
                String fileName = i + "_" + color + ".png";
                drawPile.push(new Card(basePath + fileName, "number", color, (byte) i));
            }

            // Add two +2 cards per color
            for (int i = 0; i < 2; i++) {
                String fileName = "2_wild_draw_" + color + ".png";
                drawPile.push(new Card(basePath + fileName, "+2", color, (byte) -1));
            }

            // Add one skip card per color
            String skipFile = "skip_" + color + ".png";
            drawPile.add(new Card(basePath + skipFile, "skip", color, (byte) -1));
        }

        // Add four +4 wild draw cards
        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card(basePath + "4_wild_draw.png", "+4", "wild", (byte) -1));
        }

        // Add four wild color change cards
        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card(basePath + "wild_color.png", "wild_color", "wild", (byte) -1));
        }
    }

    /**
     * Shuffles the draw pile randomly.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    /**
     * Draws the top card from the draw pile.
     * If the draw pile is empty, reshufless the discard pile (except the top card).
     *
     * @return the drawn card
     */
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleFromDiscardPile();
        }
        return drawPile.isEmpty() ? null : drawPile.pop();
    }

    /**
     * Adds a card to the top of the discard pile.
     *
     * @param card the card to discard
     */
    public void discard(Card card) {
        discardPile.push(card);
    }

    /**
     * Returns the card currently at the top of the discard pile.
     *
     * @return the top discarded card, or null if the pile is empty
     */
    public Card getTopDiscard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
    }

    /**
     * Reshuffles the discard pile into the draw pile, excluding the top discard card.
     * Ensures continuous gameplay when the draw pile runs out.
     */
    private void reshuffleFromDiscardPile() {
        if (discardPile.size() <= 1) return;

        Card top = discardPile.pop();
        List<Card> remainingDiscards = new ArrayList<>(discardPile);
        Collections.shuffle(remainingDiscards);

        drawPile.addAll(remainingDiscards);
        discardPile.clear();
        discardPile.push(top);
    }

    /** @return the number of cards remaining in the draw pile */
    public int getDrawPileSize() { return drawPile.size(); }

    /** @return the number of cards in the discard pile */
    public int getDiscardPileSize() { return discardPile.size(); }

    /**
     * Prints all cards currently in the draw pile.
     * Useful for debugging or game tracking.
     */
    public void printDrawPile() {
        System.out.println("Draw Pile:");
        for (Card card : drawPile) {
            System.out.println(" " + card);
        }
    }

    /**
     * Prints all cards currently in the discard pile.
     * Useful for debugging or game tracking.
     */
    public void printDiscardPile() {
        System.out.println("Discard Pile:");
        for (Card card : discardPile) {
            System.out.println(" " + card);
        }
    }

}