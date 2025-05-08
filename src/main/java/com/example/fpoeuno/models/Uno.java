package com.example.fpoeuno.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that manages the core logic of the UNO game, including deck creation,
 * shuffling, and dealing cards to players.
 */
public class Uno {

    private final Pile mainDeck = new Pile();  // Main draw pile
    private final Pile humanHand = new Pile(); // Human player's hand
    private final Pile aiHand = new Pile();    // AI player's hand
    private final List<Cards> allCards;        // List of all UNO cards

    /**
     * Constructor that initializes the card list.
     */
    public Uno() {
        allCards = new ArrayList<>();
    }

    /**
     * Adds numeric cards (0 to 9) for a given color.
     * @param color the color of the cards
     */
    private void addNumberCardsByColor(String color) {
        for (int i = 0; i < 9; i++) {
            String filename = i + "_" + color + ".png";
            allCards.add(new Cards(filename, "number", color, (byte) i));
        }
    }

    /**
     * Adds special cards like reverse and draw2 for a given color.
     * @param color the color of the special cards
     */
    private void addSpecialCardsByColor(String color) {
        allCards.add(new Cards("reverse_" + color + ".png", "reverse", "", (byte) 0));
        for (int i = 0; i < 2; i++) {
            allCards.add(new Cards("2_wild_draw_" + color + ".png", "draw2", color, (byte) 0));
        }
    }

    /**
     * Adds colorless wild and +4 cards to the deck.
     */
    private void addGeneralSpecialCards() {
        for (int i = 0; i < 4; i++) {
            allCards.add(new Cards("4_wild_card.png", "draw4", "", (byte) 0));
            allCards.add(new Cards("wild.png", "wild", "", (byte) 0));
        }
    }

    /**
     * Adds both number and special cards (reverse, draw2) for a given color.
     * @param color the color of the cards (e.g., "red")
     */
    private void addCardsByColor(String color) {
        addNumberCardsByColor(color);
        addSpecialCardsByColor(color);
    }

    /**
     * Initializes the full UNO card set and stores them in the 'allCards' list.
     */
    public void initializeCards() {
        addNumberCardsByColor("red");
        addNumberCardsByColor("blue");
        addNumberCardsByColor("yellow");
        addNumberCardsByColor("green");
        addGeneralSpecialCards();
    }

    /**
     * Shuffles all initialized cards and pushes them into the main deck.
     */
    private void generateMainDeck() {
        Collections.shuffle(allCards);
        for (Cards card : allCards) {
            mainDeck.push(card);
        }
    }

    /**
     * Deals a specific number of cards to both the human and AI players.
     *
     * @param cardsPerPlayer number of cards to deal to each player
     */
    public void dealInitialCards(int cardsPerPlayer) {
        for (int i = 0; i < cardsPerPlayer; i++) {
            if (!mainDeck.isEmpty()) {
                humanHand.push(mainDeck.pop());
            }
            if (!mainDeck.isEmpty()) {
                aiHand.push(mainDeck.pop());
            }
        }
    }

}