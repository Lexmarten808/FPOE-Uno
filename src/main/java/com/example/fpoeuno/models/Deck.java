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

    private final Stack<Card> drawPile;
    private final Stack<Card> discardPile;

    public Deck() {
        this.drawPile = new Stack<>();
        this.discardPile = new Stack<>();
        initializeDeck();
        shuffle();
    }

    public void initializeDeck() {
        String basePath = "images/cards/";
        String[] colors = {"red", "yellow", "green", "blue"};

        for (String color : colors) {
            for (int i = 0; i <= 9; i++) {
                String image = basePath + i + "_" + color + ".png";
                drawPile.push(new Card(color, String.valueOf(i), image));
            }

            drawPile.push(new Card(color, "wild_draw_2", basePath + "2_wild_draw_" + color + ".png"));
            drawPile.push(new Card(color, "wild_draw_2", basePath + "2_wild_draw_" + color + ".png"));

            drawPile.push(new Card(color, "skip", basePath + "skip_" + color + ".png"));
        }

        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card("wild", "wild", basePath + "wild.png"));
            drawPile.push(new Card("wild", "wild_draw_4", basePath + "4_wild_draw.png"));
        }
    }

    public void shuffle() {
        Collections.shuffle(drawPile);
    }

    public Card drawCard() {
        if (drawPile.isEmpty()) {
            reshuffleFromDiscardPile();
        }
        return drawPile.isEmpty() ? null : drawPile.pop();
    }

    public void discardCard(Card card) {
        discardPile.push(card);
    }

    private void reshuffleFromDiscardPile() {
        if (discardPile.size() <= 1) return;

        Card top = discardPile.pop();
        List<Card> remainingDiscards = new ArrayList<>(discardPile);
        Collections.shuffle(remainingDiscards);

        drawPile.addAll(remainingDiscards);
        discardPile.clear();
        discardPile.push(top);
    }

    public Card getTopDiscard() {
        return drawPile.isEmpty() ? null : discardPile.peek();
    }

    public Stack<Card> getDrawPile() { return drawPile; }
    public Stack<Card> getDiscardPile() { return discardPile; }


    public void printDeck() {
        System.out.println("***********draw pile");
        for (Card card : drawPile) {
            System.out.println(card);
        }
        System.out.println("***********discard pile");
        for (Card card : discardPile) {
            System.out.println(card);
        }
    }

}