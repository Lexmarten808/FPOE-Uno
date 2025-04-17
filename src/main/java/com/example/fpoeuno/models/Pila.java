package com.example.fpoeuno.models;

import java.util.Stack;
//this class manages the piles
public class Pila {
    private Stack<Cards> cards=new Stack<Cards>();
    public void push(Cards card){cards.push(card);}
    public Cards pop(){return cards.pop();}
    public Cards peek(){return cards.peek();}
    public boolean isEmpty(){return cards.isEmpty();}
}
