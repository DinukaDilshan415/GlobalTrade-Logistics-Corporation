package me.dinuka.gtlc.log;

import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ApplicationLogger {

    private static final Logger LOGGER =
            Logger.getLogger("GlobalTrade");

    static {
        try {
            DailyFileHandler fileHandler =
                    new DailyFileHandler(
                            "C:/GlobalTrade/logs"
                    );

            fileHandler.setLevel(Level.ALL);
            LOGGER.addHandler(fileHandler);
            LOGGER.setLevel(Level.ALL);
            LOGGER.setUseParentHandlers(false);
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private ApplicationLogger() {
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
