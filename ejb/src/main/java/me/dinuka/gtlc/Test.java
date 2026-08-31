package me.dinuka.gtlc;

import me.dinuka.gtlc.enums.AlertStatus;
import me.dinuka.gtlc.util.EnvConfig;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ThreadLocalRandom;

public class Test {
    public static void main(String[] args) {
        String BASE_DIR = EnvConfig.get("DB_BACKUP_LOCATION");

        System.out.println(BASE_DIR);
    }
}
