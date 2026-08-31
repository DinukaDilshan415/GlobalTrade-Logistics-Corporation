package me.dinuka.gtlc.schedule;

import me.dinuka.gtlc.log.ApplicationLogger;
import me.dinuka.gtlc.util.EnvConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

public class DatabaseBackupService {

    private static final Logger LOGGER = ApplicationLogger.getLogger();

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HHmmss");

    public void createBackup() throws Exception {

        String host = EnvConfig.get("DB_HOST");
        String database = EnvConfig.get("DB_NAME");
        String port = EnvConfig.get("DB_PORT");
        String username = EnvConfig.get("DB_USERNAME");
        String password = EnvConfig.get("DB_PASSWORD");
        String backupLocation = EnvConfig.get("DB_BACKUP_LOCATION");

        LocalDateTime now = LocalDateTime.now();

        String timestamp = now.format(FORMATTER);

        Path backupDirectory = Paths.get(backupLocation);

        Files.createDirectories(backupDirectory);

        Path backupFile = backupDirectory.resolve(database + "_" + timestamp + ".sql");

        LOGGER.info("Creating database backup: " + backupFile);

        ProcessBuilder processBuilder =
                new ProcessBuilder(
                        "mysqldump",
                        "--host=" + host,
                        "--port=" + port,
                        "--user=" + username,
                        "--result-file=" + backupFile.toString(),
                        database
                );

        processBuilder.environment().put("MYSQL_PWD", password);

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();

        try (BufferedReader reader =
                     new BufferedReader(
                             new InputStreamReader(
                                     process.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line)
                        .append(System.lineSeparator());
            }
        }

        int exitCode =
                process.waitFor();

        if (exitCode != 0) {
            LOGGER.severe("mysqldump failed. Exit code: " + exitCode + "\n" + output);
            Files.deleteIfExists(backupFile);
            throw new RuntimeException(
                    "Database backup failed."
            );
        }
        LOGGER.info("Database backup completed: " + backupFile);
    }
}
