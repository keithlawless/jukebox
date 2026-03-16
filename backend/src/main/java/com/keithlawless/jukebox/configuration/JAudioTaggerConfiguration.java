package com.keithlawless.jukebox.configuration;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class JAudioTaggerConfiguration {
    @EventListener(ContextRefreshedEvent.class)
    public void goEasyOnTheLogging() {

        Logger jaudiotaggerLogger = Logger.getLogger("org.jaudiotagger");
        jaudiotaggerLogger.setLevel(Level.WARNING);
        
        java.util.logging.LogManager logManager = java.util.logging.LogManager.getLogManager();
        java.util.Enumeration<String> loggerNames = logManager.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            String loggerName = loggerNames.nextElement();
            if (loggerName.startsWith("org.jaudiotagger")) {
                Logger logger = Logger.getLogger(loggerName);
                logger.setLevel(Level.WARNING);
            }
        }
    }
}
