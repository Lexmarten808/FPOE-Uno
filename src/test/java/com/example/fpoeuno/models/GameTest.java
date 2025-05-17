package com.example.fpoeuno.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Game class.
 * This test suite validates the initial state of a new game ensures
 * that game state changes (turn, established color, and value) are applied correctly.
 */
class GameTest {

    private Game game;

    /**
     * Initializes a new Game instance before each test.
     */
    @BeforeEach
    void setUp() {
        game = new Game();
    }

    /**
     * Tests the initial state of the game.
     * By default, the current turn should be "Human", and no color or value should be established.
     */
    @Test
    void testGetEstablishedConditions() {
        assertEquals("Human", game.getCurrentTurn());
        assertNull(game.getEstablishedColor());
        assertNull(game.getEstablishedValue());
    }

    /**
     * Tests the ability to set the game's current turn, established color, and established value.
     * Ensures that the state updates are applied and retrieved correctly.
     */
    @Test
    void testSetEstablishedConditions() {
        String newColor = "blue";
        String newValue = "9";
        String newTurn = "Computer";

        game.setEstablishedColor(newColor);
        game.setEstablishedValue(newValue);
        game.setCurrentTurn(newTurn);

        assertEquals(newColor, game.getEstablishedColor());
        assertEquals(newValue, game.getEstablishedValue());
        assertEquals(newTurn, game.getCurrentTurn());
    }

}