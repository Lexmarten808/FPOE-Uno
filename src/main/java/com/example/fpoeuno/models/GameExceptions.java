package com.example.fpoeuno.models;

// Clase contenedora de excepciones del juego
public class GameExceptions {

    // Excepción para cuando se juega una carta invalida
    public static class InvalidCardPlayException extends Exception {
        public InvalidCardPlayException(String message) {
            super(message);
        }
    }

    // InvalidTurnException.java
    public static class InvalidTurnException extends Exception {
        public InvalidTurnException(String message) {
            super(message);
        }
    }

    public static class NotYourTurnException extends Exception {
        public NotYourTurnException(String message) {
            super(message);
        }
    }

    public static class WildColorPendingException extends Exception {
        public WildColorPendingException(String message) {
            super(message);
        }
    }

    public static class CannotDrawCardException extends Exception {
        public CannotDrawCardException(String message) {
            super(message);
        }
    }
}