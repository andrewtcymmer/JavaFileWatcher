package com.github.andrewtcymmer.javafilewatcher;

/**
 * TODO: add javadoc here.
 */
public interface IFileWatcher {
    void start(AbstractValidator validator) throws Exception;
    void stop() throws Exception;
    void setInputDirectory(String path);
    String getInputDirectory();
    void setOutputDirectory(String path);
    String getOutputDirectory();
    void setErrorDirectory(String path);
    String getErrorDirectory();
}
