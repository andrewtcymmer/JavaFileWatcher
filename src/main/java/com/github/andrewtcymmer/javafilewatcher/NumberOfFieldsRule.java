package com.github.andrewtcymmer.javafilewatcher;

/**
 * Checks that the line of input contains exactly the specified number of fields.
 * It does this by splitting the string on the comma character and counting the size of the array.
 * Reminder, that lack of a visibility modifier on the class means it is package-private.
 */
class NumberOfFieldsRule extends AbstractRule {
    private int numFields;

    public NumberOfFieldsRule(int numFields) {
        super(String.format("Expected %d fields, but did not find that exact number.", numFields));
        this.numFields = numFields;
    }

    @Override
    boolean run(String value) {
        String[] fields = value.split(",");
        return fields.length == this.numFields;
    }
}
