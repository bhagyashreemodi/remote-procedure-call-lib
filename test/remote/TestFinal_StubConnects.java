package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;

/** Performs basic tests on the public interface of {@link remote.StubFactory}.

    <p>
    This test is best performed after <code>TestCheckpoint_ServiceRuns</code>.

    <p>
    The test verifies that a stub connects to the address it is created with.
 */
public class TestFinal_StubConnects extends Test {
    /** Test notice. */
    public static final String notice = 
        "checking stub connects to provided address";
    /** Prerequisites. */
    public static final Class[] prerequisites = new Class[] { TestFinal_StubInterface.class };

    /** Service address used for the creation of stubs. */
    private String address;
    /** Test service used during the construction of stubs. */
    private Service<SimpleInterface> service;

    /** Initializes the test and sets up the test service. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);

        // Create a test object and service
        try {
            service = new Service<SimpleInterface>(SimpleInterface.class, new SimpleObject(), port);
            service.start();
        } catch(Throwable t) {
            throw new TestFailed("unable to create test service", t);
        }
    }

    /** Checks that a stub connects to the service it was created for. */
    @Override
    protected void perform() throws TestFailed {
    
        // Create the stub.
        SimpleInterface stub;
        try {
            stub = StubFactory.create(SimpleInterface.class, address);
        } catch(Throwable t) {
            throw new TestFailed("unable to create stub for connecting to test service", t);
        }

        // Attempt to make a call to the Service.
        try {
            stub.method(false);
        } catch(RemoteObjectException e) {
        } catch(Throwable t) {
            throw new TestFailed("exception when attempting to connect to service", t);
        }
    }

    /** Stops the test service. */
    @Override
    protected void clean() {
        service.stop();
        service = null;
    }
}

