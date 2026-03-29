package com.keithlawless.jukebox.services;

import com.google.common.net.UrlEscapers;
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

    private static final String UNKNOWN = "Unknown";

    public MediaMeta readTags(String mrl) {
        String targetMrl;

        if (mrl != null) {
            targetMrl = UrlEscapers.urlFragmentEscaper().escape(mrl);
        }
        else {
            return null;
        }

        MediaMeta mediaMeta = new MediaMeta();
        mediaMeta.setMrl(targetMrl);

        // If an internet stream, don't attempt to extract tags
        if(!targetMrl.startsWith("file")) {
            mediaMeta.setArtist("Internet Radio");
            mediaMeta.setAlbum("");
            mediaMeta.setTitle("");
            mediaMeta.setTrackNumber("");
            return mediaMeta;
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
            mediaMeta.setArtist(tag.getFirst(FieldKey.ARTIST));
            mediaMeta.setAlbum(tag.getFirst(FieldKey.ALBUM));
            mediaMeta.setTitle(tag.getFirst(FieldKey.TITLE));
            mediaMeta.setTrackNumber(tag.getFirst(FieldKey.TRACK));
        }
        catch(Exception e) {
            logger.info("Exception in readTags(): " + e.toString());
            mediaMeta.setArtist(UNKNOWN);
            mediaMeta.setAlbum(UNKNOWN);
            mediaMeta.setTitle(UNKNOWN);
            mediaMeta.setTrackNumber("");
        }

        return mediaMeta;
    }

    public Artwork getArtwork(String mrl) {
        File file;

        try {
            String targetMrl;
            targetMrl = UrlEscapers.urlFragmentEscaper().escape(mrl);

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
            logger.info("Exception in getArtwork(): " + e.toString());
        }

        return null;
    }

}
