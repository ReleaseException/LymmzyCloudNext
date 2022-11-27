package com.releasenetworks.logger;

import de.gommzy.cloud.config.Config;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger {

    public static void log(String message, Level level, Object... args) {
        System.out.println("[" + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + "] - [" +level.toString().toUpperCase(Locale.ROOT) + "] - " + String.format(message, args));
    }



    public enum Level {
        DEBUG,
        INFO,
        ERROR,
        COMMAND
    }

}
