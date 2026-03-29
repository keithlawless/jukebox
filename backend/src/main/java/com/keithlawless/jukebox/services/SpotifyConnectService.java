package com.keithlawless.jukebox.services;

import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.enums.AudioSource;

import java.util.logging.Logger;

@Service
public class SpotifyConnectService {
    private static final Logger logger = Logger.getLogger(SpotifyConnectService.class.getName());

    private AudioSource currentAudioSource = AudioSource.LOCAL;
    private boolean spotifyActive = false;

    public synchronized void activateSpotifyMode() {
        logger.info("Activating Spotify Connect mode");
        currentAudioSource = AudioSource.SPOTIFY;
        spotifyActive = true;
    }

    public synchronized void deactivateSpotifyMode() {
        logger.info("Deactivating Spotify Connect mode");
        currentAudioSource = AudioSource.LOCAL;
        spotifyActive = false;
    }

    public synchronized boolean isSpotifyActive() {
        return spotifyActive;
    }

    public synchronized AudioSource getCurrentAudioSource() {
        return currentAudioSource;
    }

    public synchronized void setAudioSource(AudioSource audioSource) {
        logger.info("Setting audio source to: " + audioSource);
        this.currentAudioSource = audioSource;
        this.spotifyActive = (audioSource == AudioSource.SPOTIFY);
    }
}
