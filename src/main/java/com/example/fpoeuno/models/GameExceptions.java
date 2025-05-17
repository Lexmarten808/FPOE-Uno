package com.example.fpoeuno.models;

/**
 * Container class for custom exceptions used in the UNO game.
 */
public class GameExceptions {

    /**
     * Thrown when a player attempts to play a card that is not allowed by the current game state.
     */
    public static class InvalidCardPlayException extends Exception {
        public InvalidCardPlayException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a player attempts to play during an invalid turn.
     */
    public static class InvalidTurnException extends Exception {
        public InvalidTurnException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a player tries to take an action when it's not their turn.
     */
    public static class NotYourTurnException extends Exception {
        public NotYourTurnException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when a wild card was player but a color has not yet been selected.
     */
    public static class WildColorPendingException extends Exception {
        public WildColorPendingException(String message) {
            super(message);
        }
    }

    /**
     * Thrown when the player is not allowed to draw a card.
     */
    public static class CannotDrawCardException extends Exception {
        public CannotDrawCardException(String message) {
            super(message);
        }
    }

}