package me.dinuka.gtlc.ejb;

import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class MonitoringFileService {

    private static final String BASE_DIRECTORY =
            "C:/GlobalTrade/monitoring/";

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void writeSystemData(String data) {

        writeToFile("system", data);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public void writePerformanceData(String data) {

        writeToFile("performance", data);
    }

    public void writeErrorData(String data) {

        writeToFile("errors", data);
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<String> readSystemData(LocalDate date) throws IOException {
        return readSystemData("system", date);
    }

    public List<String> readPerformanceData(LocalDate date) throws IOException {
        return readSystemData("performance", date);
    }

    private void writeToFile(String category, String data) {

        LocalDate today = LocalDate.now();

        Path directory = Paths.get(
                BASE_DIRECTORY,
                category
        );

        Path file = directory.resolve(
                today + ".txt"
        );

        try {

            Files.createDirectories(directory);

            Files.writeString(
                    file,
                    data + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private List<String> readSystemData(String category, LocalDate date) throws IOException {

        Path file = Paths.get(
                BASE_DIRECTORY,
                category,
                date + ".txt"
        );

        if (!Files.exists(file)) {
            return Collections.emptyList();
        }

        return Files.readAllLines(file);
    }
}
