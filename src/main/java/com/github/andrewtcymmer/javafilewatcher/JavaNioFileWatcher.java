package com.github.andrewtcymmer.javafilewatcher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;

/**
 * Wrapper around the Java WatchService which detects changes in file system paths.
 * This class will register a WatchService instance on the configured inputDirectory (see spring.xml).
 */
public class JavaNioFileWatcher implements IFileWatcher {
    private WatchService watchService;
    private WatchKey watchKey;
    private String inputDir;
    private String outputDir;
    private String errorDir;
    private volatile boolean running;

    public JavaNioFileWatcher() {
        running = false;
    }

    //TODO: see https://docs.oracle.com/javase/tutorial/essential/io/notification.html
    @Override
    public void start(AbstractValidator validator) throws IOException, InterruptedException {
        if (!running) {
            Path path = Paths.get(inputDir);
            watchService = path.getFileSystem().newWatchService();
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
            running = true;
            while(running) {
                watchKey = watchService.take();
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    // assume that any event is a file creation event, since that is the only kind the service is registered to listen to.
                    process(validator);
                }
                if (!watchKey.reset()) {
                    watchKey.cancel();
                    watchService.close();
                }
            }
            // clean up tasks
            if (watchKey != null && !watchKey.reset()) {
                watchKey.cancel();
            }
            if (watchService != null) {
                watchService.close();
            }
        }
    }

    @Override
    public void stop() {
        running = false;
    }

    @Override
    public void setInputDirectory(String path) {
        this.inputDir = path;
    }

    @Override
    public String getInputDirectory() {
        return this.inputDir;
    }

    @Override
    public void setOutputDirectory(String path) {
        this.outputDir = path;
    }

    @Override
    public String getOutputDirectory() {
        return this.outputDir;
    }

    @Override
    public void setErrorDirectory(String path) {
        this.errorDir = path;
    }

    @Override
    public String getErrorDirectory() {
        return this.errorDir;
    }

    //TODO: this method could be better abstracted. It should belong in a class (call it a FileProcessor) which can be injected into the constructor to decouple processing from listening. Hit a time constraint here...
    private void process(AbstractValidator validator) {
        //TODO: open and parse file. In absence of that (given time limit) mock the input.
        String fileName = "hard-coded-fake-file-name.csv";
        String input = "12345678,Bobby,Drops,Tables,555-555-5555\n12345679,Broken Input";
        String[] lines = input.split("\n");

        int lineCounter = 0;
        for (String line : lines) {
            lineCounter++;
            List<String> violatedRuleMessages = validator.run(line);
            if (violatedRuleMessages.isEmpty()) {
                //TODO: actually convert input to json instead of faking it
                String jsonBlob = "[{\"id\": 1234567, \"name\": { \"first\": \"Bobby\", \"middle\": \"Drops\", \"last\":\"Tables\"}, \"phone\": \"555-555-5555\"}]";
                writeSuccess(jsonBlob);
            } else {
                String messages = "";
                for (String message : violatedRuleMessages) {
                    if (messages.isEmpty()) {
                        messages = message;
                    } else {
                        messages += " " + message;
                    }
                }
                String error = String.format("File: '%s' Line: %d   Message(s): %s", fileName, lineCounter, messages);
                writeError(error);
            }
        } //end for lines
    }

    private void writeError(String message) {
        File errorFile = new File(this.errorDir + "\\errors.txt");
        try {
            if (!errorFile.exists()) {
                errorFile.createNewFile();
            }
            FileWriter writer = new FileWriter(errorFile.getAbsolutePath());
            writer.append(message);
            writer.close();
        } catch (IOException ex) {
            System.out.println("Failed writing error to file " + errorFile.getAbsolutePath());
            // ignore; not the best strategy, but something to come back to later.
        }
    }

    private void writeSuccess(String jsonBlob) {
        File successFile = new File(this.outputDir + "\\successes.json");
        try {
            if (!successFile.exists()) {
                successFile.createNewFile();
            }
            FileWriter writer = new FileWriter(successFile.getAbsolutePath());
            writer.append(jsonBlob);
            writer.close();
        } catch (IOException ex) {
            System.out.println("Failed to write success to file " + successFile.getAbsolutePath());
        }
    }
}
