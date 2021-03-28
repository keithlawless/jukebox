package com.keithlawless.jukebox.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keithlawless.jukebox.entity.RadioStations;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class InternetRadioService {
    private static final Logger logger = Logger.getLogger(InternetRadioService.class.getName());
    private static String dataFile = "/data/stations.json";

    public RadioStations getStations() {
        RadioStations stations = new RadioStations();
        try {
            String data = readFileFromResources(dataFile);

            ObjectMapper objectMapper = new ObjectMapper();
            stations = objectMapper.readValue(data, RadioStations.class);
        }
        catch(URISyntaxException e) {
            logger.info("URISyntaxException caught: " + e.toString());
        }
        catch(IOException e) {
            logger.info("IOException caught: " + e.toString());
        }

        return stations;

    }

    private String readFileFromResources(String filename) throws URISyntaxException, IOException {
        String dataFilePath = dataFilePath();
        InputStream in = new URL(dataFilePath).openStream();
        return new String(IOUtils.toByteArray(in));
    }

    private String dataFilePath() {
        ClassPathResource dataFileResource = new ClassPathResource(dataFile);
        try {
            URL url = dataFileResource.getURL();
            return url.toURI().toASCIIString();
        }
        catch(IOException ioe) {
            logger.info("IOException in dataFilePath: " + ioe.toString());
        }
        catch(URISyntaxException e) {
            logger.info("URISyntaxException in dataFilePath: " + e.toString());
        }

        return "";
    }
}
