package com.example.fpoeuno.models;

import java.util.Collections;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

/**
 * Manages the UNO card deck, including the draw and discard piles.
 * Handles deck initialization, shuffling, drawing, discarding, and reshuffling when necessary.
 */
public class Deck {

    private final Stack<Card> drawPile;
    private final Stack<Card> discardPile;

    /** Constructs the deck and initializes it with all standard UNO cards. */
    public Deck() {
        this.drawPile = new Stack<>();
        this.discardPile = new Stack<>();
        initializeDeck();
        shuffle();
    }

    /**
     * Initializes the draw pile with a full set of UNO cards, including numbers,
     * skips, wilds, and draw cards.
     */
    public void initializeDeck() {
        String basePath = "images/cards/";
        String[] colors = {"red", "yellow", "green", "blue"};

        for (String color : colors) {
            // Add number cards 0-9 (one of each per color)
            for (int i = 0; i <= 9; i++) {
                String image = basePath + i + "_" + color + ".png";
                drawPile.push(new Card(color, String.valueOf(i), image));
            }

            // Add two Wild Draw 2 cards per color
            drawPile.push(new Card(color, "wild_draw_2", basePath + "2_wild_draw_" + color + ".png"));
            drawPile.push(new Card(color, "wild_draw_2", basePath + "2_wild_draw_" + color + ".png"));

            // Add one Skip card per color
            drawPile.push(new Card(color, "skip", basePath + "skip_" + color + ".png"));
        }

        // Add wild and wild draw 4 cards (4 of each)
        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card("wild", "wild", basePath + "wild.png"));
            drawPile.push(new Card("wild", "wild_draw_4", basePath + "4_wild_draw.png"));
        }
    }

    /**
     * Shuffles the draw pile randomly.
     */
    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    /**
     * Draws a card from the draw pile. If the pile is empty, reshuffles from the discard pile.
     *
     * @return the drawn card, or null if no cards are available.
     */
    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleFromDiscardPile();
        }
        return drawPile.isEmpty() ? null : drawPile.pop();
    }

    /**
     * Places a card on the discard pile.
     *
     * @param card the card to discard.
     */
    public void discardCard(Card card) { discardPile.push(card); }

    /**
     * Reshuffles the discard pile back into the draw pile, keeping the top card in place.
     * Only happens if the draw pile is empty.
     */
    private void reshuffleFromDiscardPile() {
        if (discardPile.size() <= 1) return;

        // Keep the top card on discard pile to maintain game continuity
        Card top = discardPile.pop();

        List<Card> remainingDiscards = new ArrayList<>(discardPile);
        Collections.shuffle(remainingDiscards);

        drawPile.addAll(remainingDiscards);
        discardPile.clear();
        discardPile.push(top);
    }

    /**
     * Returns the discard pile stack for inspection or UI updates.
     *
     * @return the discard pile.
     */
    public Stack<Card> getDiscardPile() { return discardPile; }

    /**
     * Prints the contents of the draw and discard piles (for debugging).
     */
    public void printDeck() {
        System.out.println("********** Draw Pile **********");
        for (Card card : drawPile) {
            System.out.println(card);
        }
        System.out.println("********** Discard Pile **********");
        for (Card card : discardPile) {
            System.out.println(card);
        }
    }

}