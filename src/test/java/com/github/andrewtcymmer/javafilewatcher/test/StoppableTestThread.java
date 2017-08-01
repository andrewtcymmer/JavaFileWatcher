package com.github.andrewtcymmer.javafilewatcher.test;

import com.github.andrewtcymmer.javafilewatcher.FileWatcherApplication;

/**
 * TODO: add javadoc here.
 * Reminder, that lack of a visibility modifier on the class means it is package-private.
 */
class StoppableTestThread extends Thread {
    private FileWatcherApplication  fileWatcher;

    public StoppableTestThread(FileWatcherApplication app) {
        super(app);
        fileWatcher = app;
    }

    public void issueStop() {
        fileWatcher.issueStop();
    }
}
