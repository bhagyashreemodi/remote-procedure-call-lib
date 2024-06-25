package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;

/** Performs basic tests on the public interface of {@link remote.Service}.

    <p>
    The test verifies that the <code>Service</code> can be started and stopped
    and accepts connections while started.
 */
public class TestCheckpoint_ServiceRuns extends Test {
    /** Test notice. */
    public static final String notice =
        "checking service runs, accepts connections, and stops";
    /** Server port used for the creation of services. */
    private final int port;
    /** The main service used for testing. */
    private final SimpleService service;
    /** Indicates whether the service has stopped. */
    private boolean stopped;

    /** Creates a <code>TestCheckpoint_ServiceRuns</code> object. */
    public TestCheckpoint_ServiceRuns() {
        Random rng = new Random(System.nanoTime());
        port = rng.nextInt(10000) + 7000;

        service = new SimpleService();
        stopped = false;
    }

    /** Performs tests with a running service.

        <p>
        This method starts the service and then stops it. In between, it probes
        to see if the service is accepting connections.
     */
    @Override
    protected void perform() throws TestFailed {
        if(probe())
            throw new TestFailed("service accepts connections before start");

        try {
            service.start();
        } catch(RemoteObjectException e) {
            throw new TestFailed("unable to start service", e);
        }

        if(!probe())
            throw new TestFailed("service refuses connections after start");

        service.stop();

        synchronized(this) {
            while(!stopped) {
                try {
                    wait();
                } catch(InterruptedException e) { }
            }
        }

        if(probe())
            throw new TestFailed("service accepts connections after stop");
    }

    /** Wakes the test code. */
    private synchronized void wake() {
        stopped = true;
        notifyAll();
    }

    /** Indicates whether it is possible to connect to the service

        @return <code>true</code> if the connection can be established, and
                <code>false</code> if it cannot be.
     */
    private boolean probe() {
        Socket socket;

        try {
            socket = new Socket("127.0.0.1", port);
        } catch(Exception e) {
            return false;
        }

        try {
            socket.close();
        } catch(Exception e) { }

        return true;
    }

    /** Stops the service and attempts to wake the test main thread. */
    @Override
    protected void clean() {
        service.stop();
        wake();
    }

    /** Derivative of <code>Service</code> which notifies the test when it stops.

        <p>
        Service thread errors are ignored because, in this test, their source is
        generally the <code>probe</code> method.
     */
    private class SimpleService extends Service<SimpleInterface> {
        /** Creates a <code>SimpleService</code> for test */
        SimpleService() {
            super(SimpleInterface.class, new SimpleObject(), port);
        }

        /** Wakes the testing main thread. */
        @Override
        protected void stopped(Throwable cause) {
            wake();
        }

        /** Handles an error in the listening thread. */
        @Override
        protected boolean listen_error(Exception e) {
            failure(new TestFailed("error in service listening thread", e));
            return false;
        }
    }
}
