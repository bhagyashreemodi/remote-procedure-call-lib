package test;

import remote.*;
import test.util.*;
import java.util.HashMap;
import java.util.Map;

/** Runs all Checkpoint tests on lab 1 remote library.

    <p>
    Tests performed are:
    <ul>
    <li>{@link test.remote.TestCheckpoint_ServiceInterface}</li>
    <li>{@link test.remote.TestCheckpoint_ServiceRuns}</li>
    </ul>
 */
public class Lab1CheckpointTests {

    /** number of times to run each test */
    private static int runsOfEachTest = 1;

    /** Runs the tests.

        @param arguments Ignored.
     */
    public static void main(String[] arguments) {

        // Create the test list, the series object, and run the test series.
        @SuppressWarnings("unchecked")
        Class<? extends Test>[] tests = new Class[] {
            test.remote.TestCheckpoint_ServiceInterface.class,
            test.remote.TestCheckpoint_ServiceRuns.class,
        };

        Map<String, Integer> points = new HashMap<>();
        
        points.put("test.remote.TestCheckpoint_ServiceInterface", 15);
        points.put("test.remote.TestCheckpoint_ServiceRuns", 20);
        
        Series series = new Series(tests, runsOfEachTest);
        SeriesReport report = series.run(3, System.out);

        // Print the report and exit with an appropriate exit status.
        report.print(System.out, points, runsOfEachTest);
        System.exit(report.successful() ? 0 : 2);
    }
}
