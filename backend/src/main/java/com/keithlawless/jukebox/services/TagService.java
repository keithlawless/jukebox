package com.keithlawless.jukebox.services;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.entity.Artwork;
import com.keithlawless.jukebox.entity.MediaMeta;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class TagService {
    private static final Logger logger = Logger.getLogger(TagService.class.getName());

    public MediaMeta readTags(String mrl) {
        String targetMrl;
        if (mrl != null) {
            targetMrl = mrl.replace(" ", "%20");
        } else {
            return null;
        }

        MediaMeta mediaMeta = new MediaMeta();
        mediaMeta.setMrl(targetMrl);

        File file;
        try {
            Path path = Paths.get(new URI(targetMrl));
            file = path.toFile();
        }
        catch(URISyntaxException e) {
            logger.info("Invalid URI.");
            return null;
        }

        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            mediaMeta.setArtist(tag.getFirst(FieldKey.ARTIST));
            mediaMeta.setAlbum(tag.getFirst(FieldKey.ALBUM));
            mediaMeta.setTitle(tag.getFirst(FieldKey.TITLE));
        }
        catch(Exception e) {
            logger.info("Exception in readTags(): " + e.toString());
        }

        return mediaMeta;
    }

    public Artwork getArtwork(String mrl) {
        String targetMrl;
        if (mrl != null) {
            targetMrl = mrl.replace(" ", "%20");
        } else {
            return null;
        }

        File file;
        try {
            Path path = Paths.get(new URI(targetMrl));
            file = path.toFile();
        }
        catch(URISyntaxException e) {
            logger.info("Invalid URI.");
            return null;
        }

        try {
            AudioFile f = AudioFileIO.read(file);
            Tag tag = f.getTag();
            if(tag.getFirstArtwork() != null){
                Artwork artwork = new Artwork();
                artwork.setMimeType(tag.getFirstArtwork().getMimeType());
                artwork.setImageBytes(tag.getFirstArtwork().getBinaryData());
                return artwork;
            }
        }
        catch(Exception e) {
            logger.info("Exception in readTags(): " + e.toString());
        }

        return null;
    }

}
