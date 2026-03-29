package com.keithlawless.jukebox.services;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.enums.AudioSource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class SpotifyConnectService implements DisposableBean {
    private static final Logger logger = Logger.getLogger(SpotifyConnectService.class.getName());

    @Value("${com.keithlawless.jukebox.spotify.librespot.path}")
    private String librespotPath;

    @Value("${com.keithlawless.jukebox.spotify.device.name}")
    private String deviceName;

    @Value("${com.keithlawless.jukebox.spotify.backend}")
    private String audioBackend;

    @Value("${com.keithlawless.jukebox.spotify.device}")
    private String audioDevice;

    @Value("${com.keithlawless.jukebox.spotify.bitrate}")
    private String bitrate;

    @Value("${com.keithlawless.jukebox.spotify.cache}")
    private String cachePath;

    private AudioSource currentAudioSource = AudioSource.LOCAL;
    private boolean spotifyActive = false;
    private Process librespotProcess = null;
    private Thread outputReader = null;
    private Thread errorReader = null;

    public synchronized void activateSpotifyMode() {
        logger.info("Activating Spotify Connect mode");
        if (spotifyActive && librespotProcess != null && librespotProcess.isAlive()) {
            logger.info("Spotify Connect is already active");
            return;
        }

        try {
            startLibrespot();
            currentAudioSource = AudioSource.SPOTIFY;
            spotifyActive = true;
            logger.info("Spotify Connect mode activated successfully");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start librespot", e);
            throw new RuntimeException("Failed to activate Spotify Connect mode", e);
        }
    }

    public synchronized void deactivateSpotifyMode() {
        logger.info("Deactivating Spotify Connect mode");
        stopLibrespot();
        currentAudioSource = AudioSource.LOCAL;
        spotifyActive = false;
        logger.info("Spotify Connect mode deactivated");
    }

    private void startLibrespot() throws IOException {
        List<String> command = new ArrayList<>();
        command.add(librespotPath);
        command.add("--name");
        command.add(deviceName);
        command.add("--backend");
        command.add(audioBackend);
        command.add("--device");
        command.add(audioDevice);
        command.add("--bitrate");
        command.add(bitrate);
        command.add("--cache");
        command.add(cachePath);

        logger.info("Starting librespot with command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);
        processBuilder.redirectErrorStream(false);
        librespotProcess = processBuilder.start();

        outputReader = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(librespotProcess.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("[librespot] " + line);
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error reading librespot output", e);
            }
        });
        outputReader.setDaemon(true);
        outputReader.start();

        errorReader = new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(librespotProcess.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    Level logLevel = Level.INFO;
                    if (line.contains(" WARN ")) {
                        logLevel = Level.WARNING;
                    } else if (line.contains(" ERROR ")) {
                        logLevel = Level.SEVERE;
                    }
                    logger.log(logLevel, "[librespot] " + line);
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Error reading librespot error stream", e);
            }
        });
        errorReader.setDaemon(true);
        errorReader.start();

        Thread processMonitor = new Thread(() -> {
            try {
                int exitCode = librespotProcess.waitFor();
                logger.warning("librespot process exited with code: " + exitCode);
                synchronized (this) {
                    spotifyActive = false;
                    currentAudioSource = AudioSource.LOCAL;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        processMonitor.setDaemon(true);
        processMonitor.start();
    }

    private void stopLibrespot() {
        if (librespotProcess != null && librespotProcess.isAlive()) {
            logger.info("Stopping librespot process");
            librespotProcess.destroy();
            try {
                if (!librespotProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    logger.warning("librespot did not stop gracefully, forcing termination");
                    librespotProcess.destroyForcibly();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                librespotProcess.destroyForcibly();
            }
            librespotProcess = null;
        }
    }

    public synchronized boolean isSpotifyActive() {
        if (librespotProcess != null && !librespotProcess.isAlive()) {
            spotifyActive = false;
            currentAudioSource = AudioSource.LOCAL;
        }
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

    @Override
    public void destroy() {
        logger.info("Cleaning up SpotifyConnectService");
        stopLibrespot();
    }
}
