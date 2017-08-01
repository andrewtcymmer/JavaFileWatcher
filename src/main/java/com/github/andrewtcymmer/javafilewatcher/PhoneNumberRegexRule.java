package com.github.andrewtcymmer.javafilewatcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks the fifth field of a csv line to match the pattern of a phone number (ex: 555-555-1234).
 * Reminder, that lack of a visibility modifier on the class means it is package-private.
 */
class PhoneNumberRegexRule extends AbstractRule {
    private static Pattern pattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");

    public PhoneNumberRegexRule() {
        super("Phone number field did not match pattern NNN-NNN-NNNN.");
    }

    @Override
    boolean run(String value) {
        String phoneNumField = getPhoneNumField(value);
        Matcher matcher = pattern.matcher(phoneNumField);
        return matcher.matches();
    }

    //TODO: likely should be promoted to the abstract rule or the validator, as this can be reused by several rules. This also raises a "design smell" in that passing the full input line to every rule may not be good for some rules.
    private String getPhoneNumField(String value) {
        if (value != null && !value.isEmpty()) {
            String[] fields = value.split(",");
            if (fields.length >= 5) {
                return fields[4];
            }
        }
        return "";
    }
}
