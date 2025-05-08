package com.example.fpoeuno.models;

import java.util.Stack;

/**
 * Represents a stack (pile) of UNO cards.
 * This class provides basic stack operations to manage card piles.
 */
public class Pile {

    private final Stack<Cards> cards = new Stack<>();

    /**
     * Adds a card to the top of the pile
     *
     * @param card the card to be pushed onto the pile
     */
    public void push(Cards card) { cards.push(card); }

    /**
     * Removes and returns the top card of the pile.
     *
     * @return the card removed from the top of the pile
     */
    public Cards pop() { return cards.pop(); }

    /**
     * Returns the top card of the pile without removing it.
     *
     * @return the top card of the pile
     */
    public Cards peek() { return cards.peek(); }

    /**
     * Checks whether the pile is empty.
     *
     * @return {@code true} if the pile is empty, {@code false} otherwise
     */
    public boolean isEmpty(){ return cards.isEmpty(); }

}