package com.example.fpoeuno.models;

import javafx.application.Platform;

import java.util.function.Consumer;

/**
 * Manages the timing mechanism for the UNO button press.
 * This class runs a background thread that waits for a specific time
 * to check whether a player has pressed the "UNO" button.
 * If the player fails to press the button in time, a penalty action is triggered.
 */
public class UnoThreadManager {

    private Thread thread;
    private boolean unoPressed;

    /**
     * Starts the countdown thread to monitor if the player presses the UNO button.
     *
     * @param player    the player being monitored.
     * @param onPenalty a callback to execute if the player fails to press UNO in time.
     */
    public void start(Player player, Consumer<Player> onPenalty) {
        unoPressed = false;

        thread = new Thread(() -> {
            try {
                Thread.sleep(4000); // Wait for 4 seconds

                // If the UNO button wasn't pressed in time, apply the penalty on the JavaFX thread
                if (!unoPressed) {
                    Platform.runLater(() -> onPenalty.accept(player));
                }
            } catch (InterruptedException e) {
                // Clean exit if the thread was interrupted
                Thread.currentThread().interrupt();
            }
        });

        thread.setDaemon(true); // Ensure thread doesn't block application exit
        thread.start();
    }

    /**
     * Signals that the player has pressed the UNO button.
     * This will stop the internal thread and cancel any penalty.
     */
    public void pressUNO() {
        unoPressed = true;
        stop();
    }

    /**
     * Stops the countdown thread if it's currently running.
     */
    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt(); // Gracefully interrupt the thread
        }
    }

    /**
     * Checks whether the UNO press monitoring thread is currently active.
     *
     * @return {@code true} if the thread is active, {@code false} otherwise.
     */
    public boolean isUNOActive() {
        return thread != null && thread.isAlive();
    }

}