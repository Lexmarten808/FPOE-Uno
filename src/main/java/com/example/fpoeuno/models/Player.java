package com.example.fpoeuno.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * Represents a player in the UNO game.
 * A player has a nickname, a flag indicating whether they are human or computer.
 * and a hand of cards they currently hold.
 */
public class Player {

    private String nickname;
    private boolean isHuman;
    private final ObservableList<Card> hand;

    /**
     * Constructs a Player with the given nickname and human flag.
     *
     * @param nickname the player's nickname.
     * @param isHuman  true if the player is human, false if computer.
     */
    public Player(String nickname, boolean isHuman) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        this.hand = FXCollections.observableArrayList();
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the Card to add.
     */
    public void addCard(Card card) { this.hand.add(card); }

    /**
     * Removes a card from the player's hand.
     *
     * @param card the Card to remove.
     */
    public void removeCard(Card card) { this.hand.remove(card); }

    /**
     * Returns the player's nickname.
     *
     * @return the player's nickname.
     */
    public String getNickname() { return nickname; }

    /**
     * Returns the player's current hand as an observable list.
     *
     * @return the observable list of card in the player's hand.
     */
    public ObservableList<Card> getHand() { return hand; }

    /**
     * Sets the player's nickname.
     *
     * @param nickname the new nickname.
     */
    public void setNickname(String nickname) { this.nickname = nickname; }

    /**
     * Sets whether the player is human or computer.
     *
     * @param isHuman true if human, false if computer.
     */
    public void setIsHuman(boolean isHuman) { this.isHuman = isHuman; }

    /**
     * Replaces the player's current hand with a new list of cards.
     *
     * @param hand an ArrayList of cards to set as the new hand.
     */
    public void setHand(ArrayList<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    /**
     * Prints all cards currently in the player's hand to the console.
     */
    public void printHand() {
        for (Card card : hand) {
            System.out.println(card);
        }
    }

}