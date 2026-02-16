package com.keithlawless.jukebox.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import com.keithlawless.jukebox.services.MediaService;
import com.keithlawless.jukebox.entity.MediaMeta;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@Component
public class CurrentSongWebSocketHandler extends TextWebSocketHandler {
    private static final Logger logger = Logger.getLogger(CurrentSongWebSocketHandler.class.getName());
    private static final long UPDATE_INTERVAL_MS = 2000;
    
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private MediaService mediaService;
    
    private volatile boolean broadcasterStarted = false;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        logger.info("WebSocket connection established. Total sessions: " + sessions.size());
        
        if (!broadcasterStarted) {
            startBroadcaster();
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        sessions.remove(session);
        logger.info("WebSocket connection closed. Total sessions: " + sessions.size());
        
        if (sessions.isEmpty()) {
            stopBroadcaster();
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    }

    private synchronized void startBroadcaster() {
        if (broadcasterStarted) {
            return;
        }
        broadcasterStarted = true;
        executor.scheduleAtFixedRate(this::broadcastCurrentSong, 0, UPDATE_INTERVAL_MS, TimeUnit.MILLISECONDS);
        logger.info("Song broadcaster started");
    }

    private synchronized void stopBroadcaster() {
        if (!broadcasterStarted) {
            return;
        }
        broadcasterStarted = false;
        executor.shutdownNow();
        logger.info("Song broadcaster stopped");
    }

    private void broadcastCurrentSong() {
        try {
            MediaMeta currentSong = mediaService.getMeta();
            String jsonMessage = objectMapper.writeValueAsString(currentSong);
            TextMessage message = new TextMessage(jsonMessage);
            
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        session.sendMessage(message);
                    } catch (IOException e) {
                        logger.warning("Failed to send message to session: " + e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.warning("Error broadcasting current song: " + e.getMessage());
        }
    }
}
