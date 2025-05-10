package com.example.fpoeuno.models;

import com.example.fpoeuno.models.enums.CardColor;
import com.example.fpoeuno.models.enums.CardType;

import java.util.Collections;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Deck {

    private Stack<Card> drawPile;
    private Stack<Card> discardPile;

    public Deck() {
        this.drawPile = new Stack<>();
        this.discardPile = new Stack<>();
        initializeDeck();
        shuffle();
    }

    public void initializeDeck() {
        String basePath = "images/cards-uno/";

        for (CardColor color : new CardColor[]{CardColor.RED, CardColor.GREEN, CardColor.BLUE, CardColor.YELLOW}) {

            // Números del 0 al 9 (1 por color)
            for (int i = 0; i <= 9; i++) {
                String fileName = i + "_" + color.name().toLowerCase() + ".png";
                drawPile.push(new Card(CardType.NUMBER, color, i, basePath + fileName));
            }

            // Dos cartas +2 por color
            for (int i = 0; i < 2; i++) {
                String fileName = "2_wild_draw_" + color.name().toLowerCase() + ".png";
                drawPile.push(new Card(CardType.DRAW_TWO, color, -1, basePath + fileName));
            }

            // Una carta skip por color
            String skipFile = "skip_" + color.name().toLowerCase() + ".png";
            drawPile.push(new Card(CardType.SKIP, color, -1, basePath + skipFile));
        }

        // Cuatro cartas comodín +4
        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card(CardType.WILD_DRAW_FOUR, CardColor.WILD, -1, basePath + "4_wild_draw.png"));
        }

        // Cuatro cartas comodí cambio de color
        for (int i = 0; i < 4; i++) {
            drawPile.push(new Card(CardType.WILD, CardColor.WILD, -1, basePath + "wild_color.png"));
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

    public void discard(Card card) {
        discardPile.push(card);
    }

    public Card getTopDiscard() {
        return discardPile.isEmpty() ? null : discardPile.peek();
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

    public void printDeckInfo() {
        System.out.println("Discard Pile (" + discardPile.size() + " cards):");
        for (Card card : discardPile) {
            System.out.println("  " + card);
        }
        System.out.println("Draw Pile (" + drawPile.size() + " cards):");
        for (Card card : drawPile) {
            System.out.println("  " + card);
        }
    }

}