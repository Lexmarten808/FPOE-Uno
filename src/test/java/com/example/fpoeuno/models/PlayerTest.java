package com.example.fpoeuno.models;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Player class.
 * These tests verify the correct behavior of player-related operations such as
 * nickname management, hand manipulation (add, remove, and set), and initial state.
 */
class PlayerTest {

    private Player player;
    private Card card1;
    private Card card2;

    /**
     * Initializes a Player instance and two sample cards before each test.
     */
    @BeforeEach
    void setUp() {
        player = new Player("Juan", true);
        card1 = new Card("red", "5", "images/cards/5_red.png");
        card2 = new Card("blue", "skip", "images/cards/skip_blue.png");
    }

    /**
     * Tests getting and setting the player's nickname.
     */
    @Test
    void testGetNicknameAndSetNickname() {
        assertEquals("Juan", player.getNickname());
        player.setNickname("Carlos");
        assertEquals("Carlos", player.getNickname());
    }

    /**
     * Tests adding a card to the player's hand.
     * Ensures the card is added and the hand size increases.
     */
    @Test
    void testAddCard() {
        player.addCard(card1);
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().contains(card1));
    }

    /**
     * Tests removing a card from the player's hand.
     * Ensures the hand is empty after the card is removed.
     */
    @Test
    void testRemoveCard() {
        player.addCard(card1);
        player.removeCard(card1);
        assertTrue(player.getHand().isEmpty());
    }

    /**
     * Tests setting a new hand for the player.
     * Verifies that the new cards are correctly reflected in the hand.
     */
    @Test
    void testSetHand() {
        ArrayList<Card> newHand = new ArrayList<>();
        newHand.add(card1);
        newHand.add(card2);

        player.setHand(newHand);

        ObservableList<Card> hand = player.getHand();
        assertEquals(2, hand.size());
        assertTrue(hand.contains(card1));
        assertTrue(hand.contains(card2));
    }

    /**
     * Tests that a new player starts with a non-null, empty hand.
     */
    @Test
    void testGetHandInitialEmpty() {
        assertNotNull(player.getHand());
        assertTrue(player.getHand().isEmpty());
    }

}