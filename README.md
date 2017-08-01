# What this is #
This was my submission to a private coding challenge to build a system that watches for files to appear in a filesystem and then performs a transformation on the data contained inside the file. The expectation for this challenge was to build as close to a production-ready system as possible within an eight hour time limit.    

# About The Project #
The project uses the following technologies:
- [Java JDK 1.8.0_131](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
- [Gradle 4.0.2](https://gradle.org/)
- [JBehave 4.x](http://jbehave.org/)
- [Spring 4.3.8](https://spring.io/)

A system running the program needs only to have the following:
- Java JRE 1.8+  (if compiling, need the JDK)
- A working internet connection

You do not need to install the other technologies listed. The included Gradle wrapper will download all the dependencies for you. The gradle task `makedist` packages all dependencies into one distributable `.jar` file so that they are available in the jar and on the default classpath at runtime.  

The first time you build the program using gradle, you'll see some console messages that indicate Gradle is downloading the dependencies declared in the `build.gradle` file.  

## How-To...

### Obtain the source
On either Windows/Mac/Linux, open a command prompt/shell and run these commands.

    cd /your/path/here
    git clone https://github.com/andrewtcymmer/JavaFileWatcher.git ./
    
### Change the configured input, output, and error directories
Edit `src/main/resources/spring.xml` and change the path properties of the `JavaNioFileWatcher` bean.  

### Build the source

    ./gradlew build
    ./gradlew makedist

### Run automated tests
The program comes with a suite of BDD-style tests. To run them:  

    ./gradlew bddTests
    
Open a browser and point it to `build/reports/tests/bddTests/index.html`.  Note that the console output will not show results of tests if the tests or the source have not changed since the last run, but the timestamp on the report page will show the most recent run date.  

If you are using an IDE, you may also run the file `src/test/java/com/github/andrewtcymmer/javafilewatcher/test/FileWatcherStories.java` as a `JUnit` test to see the results in the IDE.  

### Run the program

    java -jar build/libs/FileWatcher-all-1.0.jar
    
At this point, the shell will appear to hang, but the program is running. To test it out:  

    touch etc/input/test.csv
    


## Assumptions
### Walkthrough of what I did
I decided to include and exclude certain features based on the time constraint and desire expressed by the instructions:  
  1. Since the directions asked for a production-style submission, the first thing I did was set up build and test facilities, even before designing a code solution.
      - Running single tests and having a report that showed individual stories and scenario pass/fail was not a priority for an 8-hour project that also needed to showcase my coding and design skills. However, they are vitally important, and I would make that a high priority on the next iteration.
  1. Looking at the requirements, I decided that the MVP needed to actually listen to changes on the file system before it did any actual processing of them, so this was my primary goal. This also had potential to add a hidden requirement for thread-safety from the overall program design. I decided to start with this. 
  1. I decided to flesh out my ideas on class relationships as abstract classes and interfaces to show the idea. Concrete classes were stubbed just well enough to get the program written. This also ensures that the contract between components works as a design while it is still easy to make changes.
  1. I spent the rest of my time making sure the test suite could actually simulate and check a real condition. Not only did this save me time from doing manual testing, but it is repeatable and matches the requirements document given to me.
      - Note that the test uses a separate thread to execute the main program, and has to, because the nature of the `WatchService` is one that runs in an infinite loop. When a single-threaded test issues the `.execute()` method, that will hang the entire test.
  1. There are a few bugs in the tests:
      - The gradle task to run them does not always actually run the tests. I am able to execute from my IDE.
      - When the tests do run, they cause the `FileWatcherApplication` to receive an exception due to thread interruption, which causes the tests to fail (IMO this is a false negative). I spent the last bits of my time trying to figure it out but I hit the time constraint before fixing it. 
      - In order to fix this properly, the design of `FileWatcherApplication` should facilitate its own shutdown mechanism (breaking the while-true loop) instead of relying on the JVM to kill the thread. Since I was short on time, I thought I'd just try using the `interrupt()` call, but it produces the observed behavior.
  1. By now I'd definitely hit the 8-hour time limit, so the last thing I focused on was making sure the program can run, writing these notes, and submitting to Git.

### Design hindsights
  1. The MVP for this project could have focused on correct output before diving deep into the file system listening. With a proper modular design, I could have written a test harness that simulated the watcher. I put a comment in the `JavaNioFileWatcher` class, `process()` method, indicating how I would prefer to solve that - essentially, abstract the processing into another class that gets dependency-injected into the `JavaNioFileWatcher`.
  1. I also should have started with Spring to use DI on the test suite as well as the main application. The test case assembles the classes roughly the same way Spring would, but it would be better to let the container manage both.
  1. Changing configuration requires rebuilding the package. The configuration would have been better served as a `paths.properties` file, or as command-line arguments.

### What I think I did well
  1. My goal was to cover as much of the solution end-to-end as I could. Given the problems I found by writing the tests the way I did early, I think it better to find those big ones now than try to fix them down the road.
  1. I used abstractions to codify the program flow. This makes extending or upgrading the program possible later; by swapping out a few classes in the spring configuration, this can change to read different format CSV files easily without breaking the existing delivered product.
