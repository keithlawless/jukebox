package com.keithlawless.jukebox.services;

import com.google.common.net.UrlEscapers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.keithlawless.jukebox.entity.Folder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Service
public class FileService {
    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    @Value("${com.keithlawless.jukebox.basedir}")
    private String baseDir;

    @Value("${com.keithlawless.jukebox.filefilter}")
    private String fileFilter;

    private final Map<String, Folder> folderCache = new HashMap<>();

    @EventListener(ApplicationReadyEvent.class)
    public void initializeCache() {
        logger.log(Level.INFO, () -> "Initializing folder cache from baseDir: " + baseDir);
        loadFolderRecursively(baseDir);
        logger.log(Level.INFO, () -> "Folder cache initialized with " + folderCache.size() + " folders");
    }

    private void loadFolderRecursively(String dirPath) {
        try {
            Folder folder = loadFolder(dirPath);
            folderCache.put(dirPath, folder);

            for (String subfolderPath : folder.getFolders()) {
                try {
                    URI subfolderUri = new URI(subfolderPath);
                    loadFolderRecursively(subfolderUri.toString());
                } catch (URISyntaxException e) {
                    logger.log(Level.WARNING, () -> "Invalid URI for subfolder: " + subfolderPath);
                }
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, () -> "Error loading folder: " + dirPath + " - " + e.toString());
        }
    }

    public Folder getFolder(String entryPoint) {
        String dir;
        if ((entryPoint != null) && (!entryPoint.equalsIgnoreCase(""))) {
            dir = UrlEscapers.urlFragmentEscaper().escape(entryPoint);
        } else {
            dir = baseDir;
        }

        if (folderCache.containsKey(dir)) {
            return folderCache.get(dir);
        }

        Folder folder = loadFolder(dir);
        folderCache.put(dir, folder);
        return folder;
    }

    private Folder loadFolder(String dir) {
        List<String> includeExtensions = Stream.of(fileFilter.split(",", -1))
                .toList();

        Folder folder = new Folder();
        folder.setMrl(dir);

        Path path;
        try {
            path = Paths.get(new URI(dir));
        }
        catch(URISyntaxException e) {
            logger.log(Level.ALL, () -> "Error: Invalid URI.");
            return folder;
        }
        catch(java.nio.file.InvalidPathException e) {
            logger.log(Level.ALL, () -> "Error: Invalid Path.");
            return folder;
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path file: stream) {
                if(file.toUri().toASCIIString().endsWith("/")) {
                    String encodedFile = fixEncoding(file.toUri().toString());
                    folder.addFolder(encodedFile);
                }
                else {
                    //Only include files that match our list of included extensions (i.e. music files)
                    String ext = com.google.common.io.Files.getFileExtension(file.getFileName().toString());
                    if(includeExtensions.contains(ext)) {
                        String encodedFile = fixEncoding(file.toUri().toString());
                        folder.addFile(encodedFile);
                    }
                }
            }
        } catch (IOException | DirectoryIteratorException x) {
            // IOException can never be thrown by the iteration.
            // In this snippet, it can only be thrown by newDirectoryStream.
            logger.log(Level.ALL, () -> "Exception caught in loadFolder(): " + x.toString());
        }

        // Sort the lists before returning.
        ArrayList<String> sortedFolders = folder.getFolders();
        ArrayList<String> sortedFiles = folder.getFiles();

        Collections.sort(sortedFolders);
        Collections.sort(sortedFiles);

        folder.setFolders(sortedFolders);
        folder.setFiles(sortedFiles);

        return folder;
    }

    private String fixEncoding(String s) {
        int i = s.indexOf('+');
        while(i > -1) {
            s = s.substring(0, i) + "%2B" + s.substring(i+1);
            i = s.indexOf('+');
        }
        i = s.indexOf('&');
        while( i > -1) {
            s = s.substring(0, i) + "%26" + s.substring(i+1);
            i = s.indexOf('&');
        }
        return s;
    }

}
