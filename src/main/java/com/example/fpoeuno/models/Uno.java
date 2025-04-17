package com.example.fpoeuno.models;

import java.util.ArrayList;
import java.util.List;

public class Uno {
    private List<Cards> cartas;

    public Uno() {
        cartas = new ArrayList<>();
    }

    public void iniciarCartas() {
        //red cards
        for (int i = 0; i <=9;i++) {
          Cards carta=new Cards(i+"_red.png","number","red", (byte) i);
          cartas.add(carta);}
        //special cards
        cartas.add(new Cards("reverse_red.png","reverse","",(byte) 0));
        for (int i = 0; i <2;i++) {
            Cards carta=new Cards("2_wild_draw_red.png","draw2","red", (byte) 0);
            cartas.add(carta);}

        //blue cards
        for (int i = 0; i <=9;i++) {
            Cards carta=new Cards(i+"_blue.png","number","blue", (byte) i);
            cartas.add(carta);}
        //special Cards
        cartas.add(new Cards("reverse_blue.png","reverse","",(byte) 0));
        for (int i = 0; i <2;i++) {
            Cards carta=new Cards("2_wild_draw_blue.png","draw2","blue", (byte) 0);
            cartas.add(carta);}

        //wild cards
        for (int i = 0; i <4;i++) {
            Cards carta=new Cards("wild.png","wild","", (byte) 0);
            cartas.add(carta);
        }
        //yellow cards
        for (int i = 0; i <=9;i++) {
            Cards carta=new Cards(i+"_yellow.png","number","yellow", (byte) i);
            cartas.add(carta);}
        //special Cards
        cartas.add(new Cards("reverse_yellow.png","reverse","",(byte) 0));
        for (int i = 0; i <2;i++) {
            Cards carta=new Cards("2_wild_draw_yellow.png","draw2","yellow", (byte) 0);
            cartas.add(carta);}
        //green cards
        for (int i = 0; i <=9;i++) {
            Cards carta=new Cards(i+"_green.png","number","green", (byte) i);
            cartas.add(carta);}
        //special Cards
        cartas.add(new Cards("reverse_green.png","reverse","",(byte) 0));
        for (int i = 0; i <2;i++) {
            Cards carta=new Cards("2_wild_draw_green.png","draw2","green", (byte) 0);
            cartas.add(carta);}
    }

}
