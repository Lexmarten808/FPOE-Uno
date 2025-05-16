package com.example.fpoeuno.models;

import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {


    private Player player;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        player = new Player("Juan", true);
        card1 = new Card("red", "5", "images/cards/5_red.png");
        card2 = new Card("blue", "skip", "images/cards/skip_blue.png");
    }

    @Test
    void testGetNicknameAndSetNickname() {
        assertEquals("Juan", player.getNickname());
        player.setNickname("Carlos");
        assertEquals("Carlos", player.getNickname());
    }

    @Test
    void testIsHumanAndSetIsHuman() {
        assertTrue(player.isHuman());
        player.setIsHuman(false);
        assertFalse(player.isHuman());
    }

    @Test
    void testAddCard() {
        player.addCard(card1);
        assertEquals(1, player.getHand().size());
        assertTrue(player.getHand().contains(card1));
    }

    @Test
    void testRemoveCard() {
        player.addCard(card1);
        player.removeCard(card1);
        assertTrue(player.getHand().isEmpty());
    }

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

    @Test
    void testGetHandInitialEmpty() {
        assertNotNull(player.getHand());
        assertTrue(player.getHand().isEmpty());
    }

}