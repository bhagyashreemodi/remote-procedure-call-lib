package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;
import java.io.FileNotFoundException;

/** Tests lossy version of connection between stub and service.

    <p>
    This test starts a service with lossy sockets then creates a stub with lossy
    sockets and repeatedly tests their interaction with network failures. The test is
    otherwise very similar to <code>TestFinal_Connection</code>.
 */
public class TestFinal_LossyConnection extends Test {
    /** Test notice. */
    public static final String notice =
        "checking fault-tolerance of connection between stub and service";

    /** Prerequisites. */
    public static final Class[] prerequisites = new Class[] { TestFinal_Connection.class };

    /** Address at which the test service will run. */
    private String address;
    /** Lossy version of the service object used in the test. */
    private LossySimpleService service;

    /** Initializes the test. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);
        service = new LossySimpleService(port);

        try {
            service.start();
        } catch(Throwable t) {
            throw new TestFailed("unable to start service", t);
        }
    }

    /** Performs the test. */
    @Override
    protected void perform() throws TestFailed {
        // Create a stub using the StubFactory with simulated loss and delay
        // and make sure it can still communicate with the server correctly.
        SimpleInterface stub;

        try {
            stub = StubFactory.create(SimpleInterface.class, address, true, true);
        } catch(Throwable t) {
            throw new TestFailed("unable to create stub", t);
        }

        // Attempt to get a whole lot of values from the stub.
        for(int i=0; i<100; i++) {
            try {
                if(stub.method(false) != null)
                    throw new TestFailed("incorrect result from stub");
            } catch(Throwable t) {
                throw new TestFailed("unexpected exception when using stub", t);
            }
            try { Thread.sleep(100); } catch (InterruptedException e) {}
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
    private class LossySimpleService extends Service<SimpleInterface> {
        /** Creates a <code>SimpleService</code> at the appropriate port, with
            a new test object. */
        LossySimpleService(int port) {
            super(SimpleInterface.class, new SimpleObject(), port, true, true);
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

