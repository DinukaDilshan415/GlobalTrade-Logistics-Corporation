package me.dinuka.gtlc.log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ErrorManager;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class DailyFileHandler extends Handler {

    private final Path logDirectory;

    private String currentDate;
    private BufferedWriter writer;

    public DailyFileHandler(String directory)
            throws IOException {

        this.logDirectory = Paths.get(directory);

        Files.createDirectories(logDirectory);

        setFormatter(new SimpleFormatter());

        openWriter();
    }

    private void openWriter() throws IOException {

        currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        Path logFile = logDirectory.resolve(
                "log-" + currentDate + ".txt"
        );

        writer = Files.newBufferedWriter(logFile,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    @Override
    public synchronized void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        try {
            String today =
                    new SimpleDateFormat("yyyy-MM-dd")
                            .format(new Date());

            if (!today.equals(currentDate)) {
                close();
                openWriter();
            }
            writer.write(getFormatter().format(record));
            writer.flush();
        } catch (IOException e) {
            reportError("Unable to write log", e, ErrorManager.WRITE_FAILURE);
        }
    }

    @Override
    public void flush() {
        try {
            if (writer != null) {
                writer.flush();
            }
        } catch (IOException e) {
            reportError("Unable to flush log", e, ErrorManager.FLUSH_FAILURE);
        }
    }

    @Override
    public void close() {
        try {
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            reportError("Unable to close log", e, ErrorManager.CLOSE_FAILURE);
        }
    }
}