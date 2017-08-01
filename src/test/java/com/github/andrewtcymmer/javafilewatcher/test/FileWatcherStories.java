package com.github.andrewtcymmer.javafilewatcher.test;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.io.StoryFinder;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.List;

/**
 * Customized runner for all JBehave scenarios in this project.
 * This can be run exactly the same way as a JUnit story.
 * If you are using an IDE, right-click on this class and "run as junit test".
 * If you are using command-line, run "gradlew test".
 * For info on JBehave, see the manual at http://jbehave.org/reference/stable/getting-started.html
 */
public class FileWatcherStories extends JUnitStories {

    @Override
    protected List<String> storyPaths() {
        return new StoryFinder().findPaths(
                CodeLocations.codeLocationFromPath("src/test/resources"),
                        "**/*.story",
                        null
        );
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withFormats(Format.IDE_CONSOLE, Format.TXT)
                        .withRelativeDirectory("../build/jbehave/")
                );
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new FileWatcherSteps());
    }
}
