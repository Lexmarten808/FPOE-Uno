package com.example.fpoeuno.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Card model class.
 * These tests validate the correctness of the constructor, getters, setters,
 * and the toString method of the Card class.
 */
class CardTest {

    private Card card;

    /**
     * Initializes a Card instance before each test.
     * Sets the card color to "Red", value to "5", and image path to "/images/red5.png".
     */
    @BeforeEach
    void setUp() {
        card = new Card("Red", "5", "/images/red5.png");
    }

    /**
     * Tests the getColor() method.
     * Ensures the card returns the correct color.
     */
    @Test
    void testGetColor() {
        assertEquals("Red", card.getColor());
    }

    /**
     * Tests the getValue() method.
     * Ensures the card returns the correct value.
     */
    @Test
    void testGetValue() {
        assertEquals("5", card.getValue());
    }

    /**
     * Tests the getImagePath() method.
     * Ensures the card returns the correct image path.
     */
    @Test
    void testGetImagePath() {
        assertEquals("/images/red5.png", card.getImagePath());
    }

    /**
     * Tests the setColor() method.
     * Verifies that the color is updated correctly and that the method returns the new color.
     */
    @Test
    void testSetColor() {
        String newColor = card.setColor("Blue");
        assertEquals("Blue", card.getColor());
        assertEquals("Blue", newColor); // verifica que el setter también retorna el valor
    }

    /**
     * Tests the toString() method.
     * Verifies the string representation of the card matches the expected format.
     */
    @Test
    void testToString() {
        String expected = "Card{Red, 5, /images/red5.png}";
        assertEquals(expected, card.toString());
    }

}