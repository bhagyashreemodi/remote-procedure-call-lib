package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;
import java.io.FileNotFoundException;

/** Checks that the service correctly handles incorrect method signatures.

    <p>
    These tests are best performed after <code>TestCheckpoint_ServiceRuns</code> and <code>TestStub</code>.
    They are:
    <ul>
    <li>The <code>Stub</code> invokes a method name that is not in the <code>Service</code>'s interface.</li>
    <li>The <code>Stub</code> invokes a method with incorrect argument type.</li>
    <li>The <code>Stub</code> invokes a method with incorrect number of arguments.</li>
    <li>The <code>Service</code> replies to a method with the incorrect argument type.</li>
    </ul>
 */
public class TestFinal_Mismatch extends Test {
    /** Test notice. */
    public static final String notice = "checking interface method signature mismatch handling";
    /** Prerequisites. */
    public static final Class[] prerequisites = new Class[] { TestFinal_StubConnects.class };

    /** Service object used in the test. */
    private Service<SimpleInterface> service;
    private String address;
    
    /** Create the mismatched stub. */
    MismatchInterface stub;
        
    /** Initializes the test. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);

        try {
            service = new Service<SimpleInterface>(SimpleInterface.class, new SimpleObject(), port);
            service.start();
        } catch(Throwable t) {
            throw new TestFailed("unable to create test service", t);
        }
    }

    /** Performs the test.

        @throws TestFailed If the test fails.
     */
    @Override
    protected void perform() throws TestFailed {

        try {
            stub = StubFactory.create(MismatchInterface.class, address);
        } catch(Throwable t) {
            throw new TestFailed("unable to create stub", t);
        }

        // invoke <code>extra_method</code> not in the <code>Service</code> interface.
        try {
            stub.extra_method();
            throw new TestFailed("Service accepted undefined method from Stub");
        } catch(RemoteObjectException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Unexpected error in undefined method call", t);
        }
        
        // invoke <code>method</code> with incorrect argument type.
        try {
            stub.method(1);
            throw new TestFailed("Service method accepted incorrect argument type from Stub");
        } catch(FileNotFoundException e) {
            throw new TestFailed("Service method accepted incorrect argument type from Stub");
        } catch(RemoteObjectException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Unexpected error in method call with wrong type", t);
        }

        // invoke <code>method</code> with additional argument/type.
        try {
            stub.method(false, 1);
            throw new TestFailed("Service accepted incorrect number of arguments in method from Stub");
        } catch(FileNotFoundException e) {
            throw new TestFailed("Service accepted incorrect number of arguments in method from Stub");
        } catch(RemoteObjectException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Unexpected error in method call with wrong number of args", t);
        }

        // Start a second thread that calls rendezvous on the test object.
        new Thread(new SecondThread()).start();

        // invoke <code>rendezvous</code> that expects different return type.
        try {
            int r = stub.rendezvous();
            throw new TestFailed("Stub accepted incorrect return type from Service");
        } catch(NullPointerException e) { 
        } catch(RemoteObjectException e) {
        } catch(Throwable t) {
            throw new TestFailed("Unexpected error in method call with wrong return type", t);
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
                int r = stub.rendezvous();
            } catch(NullPointerException e) {
            } catch(RemoteObjectException e) {
            } catch(Throwable t) {
                failure(new TestFailed("unable to rendezvous in second thread", t));
            }
        }
    }

    /** Interface for the <code>Stub</code> that doesn't match the <code>Service</code> interface.
    
        <p>
        This interface is used to test error handling.
     */
    private interface MismatchInterface {
        /** Tests mismatched arguments.

            @param throw_exception takes <code>int</code> type instead of / in addition to the 
                                   <code>boolean</code> expected by the <code>Service</code>.
            @return <code>null</code>.
            @throws FileNotFoundException If the argument is <code>&gt; 0</code>.
            @throws RemoteObjectException If the call cannot be complete due to a network error.
         */
        public Object method(int exception_number)
            throws RemoteObjectException, FileNotFoundException;
        public Object method(boolean throw_exception, int exception_number)
            throws RemoteObjectException, FileNotFoundException;

        /** Tests mismatched return type.

            @return <code>int</code> value representing error code.
            @throws RemoteObjectException If the call cannot be complete due to a network error.
         */
        public int rendezvous() throws RemoteObjectException;
        
        /** Extra method not included in <code>Service</code> interface.

            @throws RemoteObjectException If the call cannot be complete due to a network error.
         */
        public void extra_method() throws RemoteObjectException;
    }
}
