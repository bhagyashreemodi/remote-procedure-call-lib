package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;
import java.io.FileNotFoundException;

/** Tests complete connection between stub and service.

    <p>
    This test starts a service then creates a stub which is tested by calling
    one of its methods.  The test covers passing arguments, transmission of
    return values, and transmission of remote exceptions.
 */
public class TestFinal_Connection extends Test {
    /** Test notice. */
    public static final String notice =
        "checking connection between stub and service";
    /** Prerequisites. */
    public static final Class[] prerequisites = new Class[] { TestFinal_StubConnects.class };

    /** Address at which the test service will run. */
    private String address;
    /** Service object used in the test. */
    private SimpleService service;

    /** Initializes the test. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);
        service = new SimpleService(port);

        try {
            service.start();
        } catch(Throwable t) {
            throw new TestFailed("unable to start service", t);
        }
    }

    /** Performs the test. */
    @Override
    protected void perform() throws TestFailed {
        // Create a stub using the StubFactory, and make sure it can connect 
        // to the service and communicate with it correctly.
        SimpleInterface stub;

        try {
            stub = StubFactory.create(SimpleInterface.class, address);
        } catch(Throwable t) {
            throw new TestFailed("unable to create stub", t);
        }

        // Attempt to get a value from the stub.
        System.out.println("====== attempting to get a value from stub =====");
        try {
            if(stub.method(false) != null)
                throw new TestFailed("incorrect result from stub");
        } catch(Throwable t) {
            throw new TestFailed("unexpected exception when using stub", t);
        }

        // Attempt to get an exception.
        System.out.println("====== attempting to get an exception =====");
        try {
            stub.method(true);
            throw new TestFailed("exception expected but not received from stub");
        } catch(FileNotFoundException e) { 
        } catch(Throwable t) {
            throw new TestFailed("unexpected exception when using stub", t);
        }
    }

    /** Stops the service. */
    @Override
    protected void clean() {
        service.stop();
        service = null;
    }

    /** Test service class that fails the test when an exception is received in
        one of the service's threads. */
    private class SimpleService extends Service<SimpleInterface> {
        /** Creates a <code>SimpleService</code> at the appropriate port, with
            a new test object. */
        SimpleService(int port) {
            super(SimpleInterface.class, new SimpleObject(), port);
        }

        /** Fails the test upon an error in the listening thread. */
        @Override
        protected boolean listen_error(Exception e) {
            failure(new TestFailed("exception in listening thread", e));
            return false;
        }

        /** Fails the test upon an error in a service thread. */
        @Override
        protected void service_error(RemoteObjectException e) {
            failure(new TestFailed("exception in service thread", e));
        }
    }
}

