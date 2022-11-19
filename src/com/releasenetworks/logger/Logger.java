package com.releasenetworks.logger;

import de.gommzy.cloud.config.Config;

import java.util.Locale;

public class Logger {

    public static void log(String message, Level level, Object... args) {
        Level configLevel = Level.valueOf(Config.getOptionAsString("logLevel"));
        if (configLevel == level) {
            System.out.println(level.toString().toUpperCase(Locale.ROOT) + " - " + String.format(message, args));
        }
    }



    public enum Level {
        DEBUG,
        INFO,
        ERROR
    }

}
