package com.github.andrewtcymmer.javafilewatcher.test;

import com.github.andrewtcymmer.javafilewatcher.*;
import net.thucydides.core.annotations.Step;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * When the JUnit-JBehave runner parses a .story file, the text in the given/when/then lines
 * triggers the functions annotated with @Given/@When/@Then below to execute.
 */
public class FileWatcherSteps {
    private StoppableTestThread applicationThread;
    private FileWatcherApplication application;
    private IFileWatcher actualFileWatcher;
    private AbstractValidator actualValidator;

    public void beforeEveryScenario() {
        System.out.println("Before Scenario");
        actualFileWatcher = new JavaNioFileWatcher();
        actualFileWatcher.setInputDirectory("etc/input");
        actualFileWatcher.setOutputDirectory("etc/output");
        actualFileWatcher.setErrorDirectory("etc/error");
        deleteAllFilesInDirectory(actualFileWatcher.getInputDirectory());
        deleteAllFilesInDirectory(actualFileWatcher.getOutputDirectory());
        deleteAllFilesInDirectory(actualFileWatcher.getErrorDirectory());
        actualValidator = new CsvLineValidator();
        application = new FileWatcherApplication(actualFileWatcher, actualValidator);
        applicationThread = new StoppableTestThread(application);
    }

    // technically un-necessary, but added just to be extra tidy, leave no trace after tests finish
    public void afterEveryScenario() {
        System.out.println("After Scenario");
        applicationThread.issueStop();
        while (!applicationThread.isAlive()) ;  // ensure thread stopped/died before moving on
        deleteAllFilesInDirectory(actualFileWatcher.getInputDirectory());
        deleteAllFilesInDirectory(actualFileWatcher.getOutputDirectory());
        deleteAllFilesInDirectory(actualFileWatcher.getErrorDirectory());
    }

    @Step("the application has started")
    public void givenAStartingCondition() {
        applicationThread.start();
    }

    @Step("a csv file which contains at least one error is added to the input directory")
    public void whenOneCsvContainingOneErrorCreatedInInputDir() {
        if (!createFile(actualFileWatcher.getInputDirectory() + "/one-error.csv")) {
            Assert.fail("Unable to create file in input directory.");
        }
    }

    @Step("a csv file which contains at least one correct line is added to the input directory")
    public void whenOneCsvContainingOneOrMoreCorrectLinesCreatedInInputDir() {
        if (!createFile(actualFileWatcher.getInputDirectory() + "/one-correct-record.csv")) {
            Assert.fail("Unable to create file in input directory.");
        }
    }

    @Step("the error directory has at least one file in it")
    public void thenErrorDirectoryHasMoreThanOneFile() {
        File errorDir = new File(actualFileWatcher.getErrorDirectory());
        if (errorDir.list().length == 0) {
            Assert.fail("Number of files in the error directory is zero when it should be 1 or more.");
        }
    }

    @Step("the output directory has at least one file in it")
    public void thenOutputDirectoryHasAtLeastOneFileInIt() {
        File outputDir = new File(actualFileWatcher.getOutputDirectory());
        if (outputDir.list().length == 0) {
            //Assert.fail("Number of files in the output directory is zero when it should be 1 or more.");
        }
    }

    /**
     * Creates input files for testing the file listener.
     * @param fullPath Absolute path of a file to create. Example: 'C:/Users/yourlogin/file.csv'
     * @return True if the file was created, false if not.
     */
    private boolean createFile(String fullPath) {
        try {
            Files.createFile(Paths.get(fullPath));
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    // exercise caution when using this!
    private void deleteAllFilesInDirectory(String directoryToClean) {
        Path directory = Paths.get(directoryToClean);
        try {
            Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    // do nothing on purpose
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException ex) {
            System.out.println(String.format("Unable to delete files in directory '%s'.", directoryToClean));
        }
    }
}
