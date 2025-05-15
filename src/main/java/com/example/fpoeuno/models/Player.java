package com.example.fpoeuno.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

public class Player {

    private String nickname;
    private boolean isHuman;
    private final ObservableList<Card> hand;

    public Player(String nickname, boolean isHuman) {
        this.nickname = nickname;
        this.isHuman = isHuman;
        this.hand = FXCollections.observableArrayList();
    }

    public void addCard(Card card) { this.hand.add(card); }
    public void removeCard(Card card) { this.hand.remove(card); }

    public String getNickname() { return nickname; }
    public boolean isHuman() { return isHuman; }
    public ObservableList<Card> getHand() { return hand; }

    public void setNickname(String nickname) { this.nickname = nickname; }
    public void setIsHuman(boolean isHuman) { this.isHuman = isHuman; }

    public void setHand(ArrayList<Card> hand) {
        this.hand.clear();
        this.hand.addAll(hand);
    }

    public void printHand() {
        for (Card card : hand) {
            System.out.println(card);
        }
    }

}