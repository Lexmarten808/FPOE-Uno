package com.example.fpoeuno.models;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private final String name;
    private final boolean isHuman;
    private final List<Card> hand;

    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.hand = new ArrayList<>();
    }

    public void drawCard(Card card) {
        if (card != null) {
            hand.add(card);
        }
    }

    public void playCard(Card card) {
        hand.remove(card);
    }

    public boolean hasPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                return true;
            }
        }
        return false;
    }

    public List<Card> getPlayableCards(Card topCard) {
        List<Card> playable = new ArrayList<>();
        for (Card card : hand) {
            if (card.canPlayOn(topCard)) {
                playable.add(card);
            }
        }
        return playable;
    }

    // --- Getters ---
    public String getName() { return name; }
    public boolean isHuman() { return isHuman; }
    public List<Card> getHand() { return hand; }

    public void printPlayerInfo() {
        System.out.println(name + " has " + hand.size() + " card(s):");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + hand.get(i));
        }
    }

}