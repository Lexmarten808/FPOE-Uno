package com.example.fpoeuno.models;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;

/**
 * Utility class for managing background music playback during the game.
 */
public class SoundManager {

    private static MediaPlayer musicPlayer;
    private static boolean isMusicPlaying = false;

    /**
     * Toggles the background music on or off.
     * If the music is currently playing, this method stops it. If not, it attempts
     * to play the specified music file in a continuous loop.
     *
     * @param fileName the name of the music file to be played.
     */
    public static void toggleMusic(String fileName) {
        try {
            // If music is already playing, stop it
            if (musicPlayer != null && isMusicPlaying) {
                musicPlayer.stop();
                isMusicPlaying = false;
                return;
            }

            // Load the music file
            URL musicURL = SoundManager.class.getResource("/com/example/fpoeuno/music/" + fileName);
            if (musicURL == null) {
                System.err.println("Music file not found: " + fileName);
                return;
            }

            // Play the music in a loop
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

}