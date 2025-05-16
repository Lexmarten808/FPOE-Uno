package com.example.fpoeuno.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class CardTest {


    private Card card;

    @BeforeEach
    void setUp() {
        card = new Card("Red", "5", "/images/red5.png");
    }

    @Test
    void testGetColor() {
        assertEquals("Red", card.getColor());
    }

    @Test
    void testGetValue() {
        assertEquals("5", card.getValue());
    }

    @Test
    void testGetImagePath() {
        assertEquals("/images/red5.png", card.getImagePath());
    }

    @Test
    void testSetColor() {
        String newColor = card.setColor("Blue");
        assertEquals("Blue", card.getColor());
        assertEquals("Blue", newColor); // verifica que el setter también retorna el valor
    }

    @Test
    void testToString() {
        String expected = "Card{Red, 5, /images/red5.png}";
        assertEquals(expected, card.toString());
    }
}