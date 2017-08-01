package com.github.andrewtcymmer.javafilewatcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Encapsulates checking a string against several AbstractRule objects to determine if the string matches the rules.
 * This base class is intended to be extended, such that a concrete class creates all of its rules in its constructor.
 * That way, simply instantiating the concrete subclass makes it ready to run.
 */
public abstract class AbstractValidator {
    private Set<AbstractRule> rules;

    public AbstractValidator() {
        rules = new HashSet<>(10);  // arbitrary initial capacity
    }

    protected void addRule(AbstractRule rule) {
        if (!rules.contains(rule)) {
            rules.add(rule);
        }
    }

    public List<String> run(String input) {
        List<String> violatedRules = new ArrayList<>();
        for (AbstractRule rule : rules) {
            if (!rule.run(input)) {
                violatedRules.add(rule.getErrorMessage());
            }
        }
        return violatedRules;
    }
}
