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

        //Disable loggers
        Logger[] pin = new Logger[]{ Logger.getLogger("org.jaudiotagger") };

        for (Logger l : pin)
            l.setLevel(Level.WARNING);
    }
}
