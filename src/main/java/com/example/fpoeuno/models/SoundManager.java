package com.example.fpoeuno.models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

public class SoundManager {

    private static MediaPlayer musicPlayer;
    private static boolean isMusicPlaying = false;

    public static void playEffect(String fileName) {
        try {
            URL soundURL = SoundManager.class.getResource("/com/example/fpoeuno/sounds/" + fileName);
            if (soundURL == null) {
                System.err.println("Sound effect not found: " + fileName);
                return;
            }

            Media media = new Media(soundURL.toString());
            MediaPlayer effectPlayer = new MediaPlayer(media);
            effectPlayer.play();

            effectPlayer.setOnEndOfMedia(effectPlayer::dispose);

        } catch (Exception e) {
            System.err.println("Error playing sound effect: " + fileName);
            e.printStackTrace();
        }
    }

    public static void toggleMusic(String fileName) {
        try {
            if (musicPlayer != null && isMusicPlaying) {
                musicPlayer.stop();
                isMusicPlaying = false;
                return;
            }

            URL musicURL = SoundManager.class.getResource("/com/example/fpoeuno/music/" + fileName);
            if (musicURL == null) {
                System.err.println("Music file not found: " + fileName);
                return;
            }

            Media media = new Media(musicURL.toString());
            musicPlayer = new MediaPlayer(media);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();
            isMusicPlaying = true;

        } catch (Exception e) {
            System.err.println("Error handling music: " + fileName);
            e.printStackTrace();
        }
    }

    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            isMusicPlaying = false;
        }
    }

}