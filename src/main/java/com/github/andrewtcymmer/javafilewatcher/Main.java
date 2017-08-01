package com.github.andrewtcymmer.javafilewatcher;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Wrapper around the entry point to the application.
 * Uses Spring framework to instantiate and run an instance of FileWatcherApplication with bean id "application".
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext appContext = new ClassPathXmlApplicationContext("spring.xml");
        FileWatcherApplication app = (FileWatcherApplication) appContext.getBean("application");
        app.run();  // single-threaded on purpose
    }
}
