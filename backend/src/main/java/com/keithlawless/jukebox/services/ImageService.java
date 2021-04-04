package com.keithlawless.jukebox.services;

import com.google.common.net.UrlEscapers;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.entity.Artwork;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.logging.Logger;

@Service
public class ImageService {
    private static final Logger logger = Logger.getLogger(ImageService.class.getName());

    @Value("${com.keithlawless.jukebox.default.image}")
    private String defaultImageLocation;

    @Autowired
    private TagService tagService;

    public Artwork fetchArtwork(String mrl) {
        Artwork artwork = new Artwork();

        String imageFile = locateImage(mrl);

        /*
         *  If we are planning on returning the default image, try to extract album
         *  art out of the media file itself.
         */
        if ((mrl.startsWith("file")) && (imageFile.endsWith(defaultImageLocation))) {
            Artwork a = tagService.getArtwork(mrl);
            if(a != null) {
                return a;
            }
        }

        if (imageFile.endsWith(".jpg")) {
            artwork.setMimeType(MediaType.IMAGE_JPEG.toString());
        } else if (imageFile.endsWith(".jpeg")) {
            artwork.setMimeType(MediaType.IMAGE_JPEG.toString());
        } else if (imageFile.endsWith(".png")) {
            artwork.setMimeType((MediaType.IMAGE_PNG.toString()));
        }

        try {
            InputStream in = new URL(imageFile).openStream();
            artwork.setImageBytes(IOUtils.toByteArray(in));
        } catch (IOException ioe) {
            logger.info("IOException in fetchArtwork(): " + ioe.toString());
        }

        return artwork;
    }

    public String locateImage(String mrl) {
        // If the mrl is not a file:// type, return the default image.
        if(mrl.startsWith("file") == false ) {
            return defaultImage();
        }

        try {
            mrl = UrlEscapers.urlFragmentEscaper().escape(mrl);

            Path path = Paths.get(new URI(mrl));
            Path parent = path.getParent();

            DirectoryStream<Path> stream = Files.newDirectoryStream(parent);
            for(Path file: stream) {
                String extension =
                        com.google.common.io.Files.getFileExtension(file.getFileName().toString());

                if(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")) {
                    return file.toUri().toASCIIString();
                }
            }
        }
        catch(URISyntaxException syntaxException) {
            logger.info("URISyntaxException in locateImage():" +
                    syntaxException.toString());
        }
        catch(IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            logger.info("Exception caught in locateImage(): " + x.toString());
        }

        ClassPathResource defaultImageResource = new ClassPathResource(defaultImageLocation);
        try {
            URL url = defaultImageResource.getURL();
            return url.toURI().toASCIIString();
        }
        catch(IOException ioe) {
            logger.info("IOException in locateImage: " + ioe.toString());
        }
        catch(URISyntaxException e) {
            logger.info("URISyntaxException in locateImage: " + e.toString());
        }

        return defaultImage();
    }

    private String defaultImage() {
        ClassPathResource defaultImageResource = new ClassPathResource(defaultImageLocation);
        try {
            URL url = defaultImageResource.getURL();
            return url.toURI().toASCIIString();
        }
        catch(IOException ioe) {
            logger.info("IOException in locateImage: " + ioe.toString());
        }
        catch(URISyntaxException e) {
            logger.info("URISyntaxException in locateImage: " + e.toString());
        }

        return "";
    }

}
