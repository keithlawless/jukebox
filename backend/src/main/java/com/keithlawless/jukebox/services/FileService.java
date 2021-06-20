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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService {
    private static final Logger logger = Logger.getLogger(FileService.class.getName());

    @Value("${com.keithlawless.jukebox.basedir}")
    private String baseDir;

    @Value("${com.keithlawless.jukebox.filefilter}")
    private String fileFilter;

    public Folder getFolder(String entryPoint) {
        List<String> includeExtensions = Stream.of(fileFilter.split(",", -1))
                .collect(Collectors.toList());

        Folder folder = new Folder();

        String dir;
        if (entryPoint != null) {
            dir = UrlEscapers.urlFragmentEscaper().escape(entryPoint);
        } else {
            dir = baseDir;
        }

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
            logger.log(Level.ALL, () -> "Exception caught in getFolder(): " + x.toString());
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
        return s;
    }

}
