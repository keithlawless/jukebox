package com.keithlawless.jukebox.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keithlawless.jukebox.entity.RadioStations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Logger;

@Service
public class InternetRadioService {
    private static final Logger logger = Logger.getLogger(InternetRadioService.class.getName());
    private static String dataFile = "data/stations.json";

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
    private static String readFileFromResources(String filename) throws URISyntaxException, IOException {
        URL resource = InternetRadioService.class.getClassLoader().getResource(filename);
        byte[] bytes = Files.readAllBytes(Paths.get(resource.toURI()));
        return new String(bytes);
    }
}
