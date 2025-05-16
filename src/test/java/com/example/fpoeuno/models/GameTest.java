package com.example.fpoeuno.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game();
    }

    // Verifica las condiciones iniciales del juego, sin color ni valor establecido y el turno es del humano.
    @Test
    void testGetEstablishedConditions() {
        assertEquals("Human", game.getCurrentTurn());
        assertNull(game.getEstablishedColor());
        assertNull(game.getEstablishedValue());
    }

    // Verifica que los cambios en las condiciones del juego están siendo aplicados
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
