package me.dinuka.gtlc.schedule;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import me.dinuka.gtlc.annotation.Logged;
import me.dinuka.gtlc.log.ApplicationLogger;

import java.util.logging.Level;
import java.util.logging.Logger;

@Logged
@Singleton
@Startup
public class DatabaseBackupScheduler {

    private static final Logger LOGGER = ApplicationLogger.getLogger();

    private final DatabaseBackupService backupService =
            new DatabaseBackupService();

    @Schedule(
            hour = "23",
            minute = "30",
            second = "0",
            persistent = false
    )
    public void performDailyBackup() {
        LOGGER.info("Starting scheduled database backup...");
        try {
            backupService.createBackup();
            LOGGER.info("Scheduled database backup completed successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Scheduled database backup failed.", e);
        }
    }
}
