package com.example.fpoeuno.models;

import javafx.application.Platform;

import java.util.function.Consumer;

public class UnoThreadManager {

    private Thread thread;
    private boolean unoPressed;

    public void start(Player player, Consumer<Player> onPenalty) {
        unoPressed = false;

        thread = new Thread(() -> {
            try {
                Thread.sleep(5000); // Espera 5 segundos

                if (!unoPressed) {
                    Platform.runLater(() -> onPenalty.accept(player));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Finalización limpia
            }
        });

        thread.setDaemon(true);
        thread.start();
    }

    public void pressUNO() {
        unoPressed = true;
        stop();
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt(); // Detiene el hilo si sigue vivo
        }
    }

    public boolean isUNOActive() {
        return thread != null && thread.isAlive();
    }
}