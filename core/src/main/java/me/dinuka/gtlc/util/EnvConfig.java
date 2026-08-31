package me.dinuka.gtlc.util;

import io.github.cdimascio.dotenv.Dotenv;

public class EnvConfig {

    private static final Dotenv dotenv;

    static {
        String localEnvPath = "F:/PROGRAM/IJ-Projects/GlobalTrade-Logistics-Corporation";

        dotenv = Dotenv.configure()
                .directory(localEnvPath)
                .filename(".env")
                .ignoreIfMissing()
                .load();
    }

    public static String get(String key) {
        return dotenv.get(key);
    }

    public static String get(String key, String defaultValue) {
        return dotenv.get(key, defaultValue);
    }
}
