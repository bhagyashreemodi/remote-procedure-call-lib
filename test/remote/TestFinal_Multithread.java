package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;

/** Checks that the service supports multiple simultaneous connections.

    <p>
    These tests are best performed after <code>TestCheckpoint_ServiceRuns</code> and
    <code>TestFinal_StubConnects</code>. This test starts a service and creates a stub of
    type <code>SimpleInterface</code>. It then calls <code>rendezvous</code> on
    the stub from two different threads. The test succeeds if both calls return.
 */
public class TestFinal_Multithread extends Test {
    /** Test notice. */
    public static final String notice = "checking service multithreading";
    /** Prerequisites. */
    public static final Class[] prerequisites = new Class[] { TestFinal_StubConnects.class };

    /** Test object used in the test. */
    private SimpleObject object;
    /** Service object used in the test. */
    private SimpleService service;
    /** Stub through which communication with the service occurs. */
    private SimpleInterface stub;
    private String address;

    /** Initializes the test. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);
        object = new SimpleObject();
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
        // Create the stub.
        try {
            stub = StubFactory.create(SimpleInterface.class, address);
        } catch(Throwable t) {
            throw new TestFailed("unable to create stub", t);
        }

        // Start a second thread that calls rendezvous on the test object.
        new Thread(new SecondThread()).start();

        // Call rendezvous on the test object.
        try {
            stub.rendezvous();
        } catch(Throwable t) {
            throw new TestFailed("unable to rendezvous in first thread", t);
        }
    }

    /** Stops the service. */
    @Override
    protected void clean() {
        service.stop();
        service = null;
    }

    /** Wakes the other thread, which is waiting for the reply from the service. */
    private class SecondThread implements Runnable {
        /** Calls the <code>wake</code> method on the remote service. */
        @Override
        public void run() {
            try {
                stub.rendezvous();
            } catch(Throwable t) {
                failure(new TestFailed("unable to rendezvous in second thread", t));
            }
        }
    }

    /** Test service class that fails the test when an exception is received in
        one of the service's threads. */
    private class SimpleService extends Service<SimpleInterface> {
        /** Creates a <code>SimpleService</code> with a new service object. */
        SimpleService(int port) {
            super(SimpleInterface.class, object, port);
        }

        /** Wakes any threads blocked in the service. */
        @Override
        protected void stopped(Throwable cause) {
            object.wake();
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

