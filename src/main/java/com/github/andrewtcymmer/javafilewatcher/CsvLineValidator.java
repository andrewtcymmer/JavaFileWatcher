package com.github.andrewtcymmer.javafilewatcher;

/**
 * Concrete validator that assumes every line of input is going to be a csv file with five fields.
 */
public class CsvLineValidator extends AbstractValidator {

    public CsvLineValidator() {
        this.addRule(new NumberOfFieldsRule(5));
        this.addRule(new PhoneNumberRegexRule());
        //TODO: add more concrete validation rule objects as requirements dictate. Some omitted due to time constraint.
    }

    //TODO: there is some room here to customize validation behavior. For example, checking that the first line is a header line and ignoring it, is specific to this kind of file so belongs here.
}
