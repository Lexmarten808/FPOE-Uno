package com.example.fpoeuno.models;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class SoundManager {
    public static Clip backgroundMusic;

    public static void playEffect(String fileName) {
        try {
            URL soundURL = SoundManager.class.getResource("/com/example/fpoeuno/sounds/" + fileName);
            if (soundURL == null) {
                System.err.println(" Archivo no encontrado: " + fileName);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println(" Error al reproducir el sonido: " + fileName);
            e.printStackTrace();
        }
    }
    public static void playMusic(String fileName) {
        try {
            URL musicURL = SoundManager.class.getResource("/com/example/fpoeuno/music/" + fileName);

            if (musicURL != null) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicURL);
                backgroundMusic = AudioSystem.getClip();
                backgroundMusic.open(audioStream);
                backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);  // Repetir indefinidamente
                backgroundMusic.start();
            } else {
                System.err.println(" Música no encontrada: " + fileName);
            }

        } catch (Exception e) {
            System.err.println(" Error al reproducir música: " + e.getMessage());
        }
    }

    public static void stopMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
}
