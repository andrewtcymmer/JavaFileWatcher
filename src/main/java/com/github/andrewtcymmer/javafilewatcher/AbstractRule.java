package com.github.andrewtcymmer.javafilewatcher;

/**
 * Encapsulates a single logical check to make on a string.
 * Reminder, that lack of a visibility modifier on the class means it is package-private.
 */
abstract class AbstractRule {
    private String errorMessage;

    public AbstractRule(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    abstract boolean run(String value);
}
