package com.keithlawless.jukebox.services;

import com.keithlawless.jukebox.entity.MediaMeta;
import com.keithlawless.jukebox.entity.MusicResourceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MediaServiceTest {

    private String sampleUri;

    @Autowired
    MediaService mediaService;

    @BeforeEach
    void findSampleSong() {
        Path sampleSongPath = Paths.get("src","test","resources", "music", "sample.flac");
        sampleUri = sampleSongPath.toUri().toString();

        assertTrue(sampleUri.startsWith("file:///"));
        assertTrue(sampleUri.endsWith("sample.flac"));
    }

    @Test
    void playOneSongImmediately() {
        MusicResourceLocator musicResourceLocation = new MusicResourceLocator(sampleUri);
        MusicResourceLocator mrl = mediaService.play(musicResourceLocation);

        assertTrue(mrl.getMrl().equalsIgnoreCase(musicResourceLocation.getMrl()));

    }
}
