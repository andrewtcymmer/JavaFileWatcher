package com.github.andrewtcymmer.javafilewatcher;

/**
 * Class which encapsulates assembly of validator, watcher, and the activity of starting the watch cycle.
 */
public class FileWatcherApplication implements Runnable {
    private IFileWatcher watcher;
    private AbstractValidator validator;

    public FileWatcherApplication(IFileWatcher watcher, AbstractValidator validator) {
        this.watcher = watcher;
        this.validator = validator;
    }

    @Override
    public void run() {
        try {
            watcher.start(this.validator);
        } catch (Exception ex) {
            System.out.println(String.format("Encountered exception during thread execution. Message: %s", ex.getMessage()));
            for (StackTraceElement element : ex.getStackTrace()) {
                System.out.println("    " + element.toString());
            }
        }
        System.out.println("Program finished execution.");
    }

    public void issueStop() {
        try {
            watcher.stop();
        } catch (Exception ex) {
            System.out.println(String.format("Encountered exception while stopping thread. Message: %s", ex.getMessage()));
            for (StackTraceElement element : ex.getStackTrace()) {
                System.out.println("    " + element.toString());
            }
        }
    }
}
