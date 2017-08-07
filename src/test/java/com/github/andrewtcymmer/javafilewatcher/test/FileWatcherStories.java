package com.github.andrewtcymmer.javafilewatcher.test;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Customized runner for all JBehave scenarios in this project.
 * This can be run exactly the same way as a JUnit story.
 * If you are using an IDE, right-click on this class and "run as junit test".
 * If you are using command-line, run "gradlew test".
 * For info on JBehave, see the manual at http://jbehave.org/reference/stable/getting-started.html
 */
@RunWith(SerenityRunner.class)
public class FileWatcherStories {
    @Steps
    FileWatcherSteps steps;

    @Before
    public void setup() {
        steps.beforeEveryScenario();
    }

    @After
    public void teardown() {
        steps.afterEveryScenario();
    }

    @Test
    public void negativeTest() {
        steps.givenAStartingCondition();
        steps.whenOneCsvContainingOneErrorCreatedInInputDir();
        steps.thenErrorDirectoryHasMoreThanOneFile();
    }

    @Test
    public void positiveTest() {
        steps.givenAStartingCondition();
        steps.whenOneCsvContainingOneOrMoreCorrectLinesCreatedInInputDir();
        steps.thenOutputDirectoryHasAtLeastOneFileInIt();
    }
}
