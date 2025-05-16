package com.example.fpoeuno.models;

// Clase contenedora de excepciones del juego
public class GameExceptions {

    // Excepción para cuando se juega una carta invalida
    public static class InvalidCardPlayException extends Exception {
        public InvalidCardPlayException(String message) {
            super(message);
        }
    }

    // Excepción para cuando un jugador intenta jugar fuera de turno
    public static class OutOfTurnException extends Exception {
        public OutOfTurnException(String message) {
            super(message);
        }
    }

}