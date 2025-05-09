package com.example.fpoeuno.models;

import com.example.fpoeuno.controllers.GameController;

import java.util.List;

/**
 * Manages the game state, including players, the deck, and initial setup.
 * Handles the flow of a basic UNO game between a human and the machine.
 */
public class Game {

    private Deck deck;
    private Player human;
    private Player machine;
    private Player currentPlayer;

    /**
     * Constructs a new UNO game instance.
     * Initializes the deck, players, deals initial cards, and starts the game.
     */
    public Game() {
        this.deck = new Deck();
        this.human = new Player("Human", true);
        this.machine = new Player("Machine", false);

        dealInitialCards();
        startGame();
    }

    /**
     * Deals the initial hand of 5 cards to each player.
     */
    private void dealInitialCards() {
        for (int i = 0; i < 5; i++) {
            human.drawCard(deck.drawCard());
            machine.drawCard(deck.drawCard());
        }
    }

    /**
     * Sets up the initial state of the game.
     * Places the first card on the discard pile and sets the current player.
     */
    public void startGame() {
        // Sacamos la primera carta del mazo
        Card firstCard = deck.drawCard();

        // Descartamos la carta en la pila de descarte
        deck.discard(firstCard);

        System.out.println("First card on the table: " + firstCard);

        // Human starts the game
        this.currentPlayer = human;

        // Game logic would continue here
    }

    /**
     * Prints both players' hands to the console.
     * Useful for debugging or test visualization.
     */
    public void printHands() {
        System.out.println("=== Human Player's Hand ===");
        human.printHand();

        System.out.println("=== Machine Player's Hand ===");
        machine.printHand();
    }

    /**
     * @return the current game deck (useful for controllers or testing)
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * @return the player whose turn it is
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * @return the human player
     */
    public Player getHuman() {
        return human;
    }

    /**
     * @return the machine player
     */
    public Player getMachine() {
        return machine;
    }

    /**
     * Prints the current state of the deck and discard pile.
     * Useful for debugging or verifying game logic.
     */
    public void printDeckState() {
        System.out.println("---- Deck State ----");
        System.out.println("Cards in draw pile: " + deck.getDrawPileSize());
        System.out.println("Cards in discard pile: " + deck.getDiscardPileSize());

        deck.printDrawPile();
        deck.printDiscardPile();
    }

}