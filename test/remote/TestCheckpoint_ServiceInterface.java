package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;

/** Performs basic tests on the public interface of {@link remote.Service}.

    <p>
    The test verifies that both <code>Service</code> constructors:
    <ul>
    <li> reject classes,
    <li> reject non-remote interfaces,
    <li> reject null arguments,
    <li> accept remote interfaces.
    </ul>
 */
public class TestCheckpoint_ServiceInterface extends Test {
    /** Test notice. */
    public static final String notice =
        "checking requirements of service constructor arguments";

    /** Server port used for the creation of services. */
    private final int port;

    /** Creates a <code>TestService</code> object. */
    public TestCheckpoint_ServiceInterface() {
        Random rng = new Random(System.nanoTime());
        port = rng.nextInt(10000) + 7000;
    }

    /** Performs the test. */
    @Override
    protected void perform() throws TestFailed {
    
        /** Ensure that a <code>Service</code> cannot be constructed from a class. */
        try {
            Service<Object> badService = new Service<Object>(Object.class, new BadObject(), port);
            throw new TestFailed("Service(Class<T>, T, int) constructor accepted a class");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int) constructor threw an unexpected " +
                                 "exception when given a class", t);
        }
        try {
            Service<Object> badService = new Service<Object>(Object.class, new BadObject(), port, false, false);
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor accepted a class");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor threw an unexpected " +
                                 "exception when given a class", t);
        }
    
    
        /** Ensures that a <code>Service</code> cannot be constructed from a non-remote interface. */
        try {
            Service<BadInterface> badService = new Service<BadInterface>(BadInterface.class, new BadObject(), port);
            throw new TestFailed("Service(Class<T>, T, int) constructor accepted a non-remote interface");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int) constructor threw an unexpected " +
                                 "exception when given a non-remote interface", t);
        }
        try {
            Service<BadInterface> badService = new Service<BadInterface>(BadInterface.class, new BadObject(), port, false, false);
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor accepted a non-remote interface");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor threw an unexpected " +
                                 "exception when given a non-remote interface", t);
        }
    
    
        /** Ensures that <code>Service</code> constructors throw <code>NullPointerException</code>
            when given <code>null</code> for the class or object parameters. */
        try {
            Service<SimpleInterface> badService = new Service<SimpleInterface>(null, new SimpleObject(), port);
            throw new TestFailed("Service(Class<T>, T, int) constructor accepted null for first argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int) constructor threw an unexpected " +
                                 "exception when given null for first argument", t);
        }        
        try {
            Service<SimpleInterface> badService = new Service<SimpleInterface>(null, new SimpleObject(), port, false, false);
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor accepted null for first argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor threw an unexpected " +
                                 "exception when given null for first argument", t);
        }
        try {
            Service<SimpleInterface> badService = new Service<SimpleInterface>(SimpleInterface.class, null, port);
            throw new TestFailed("Service(Class<T>, T, int) constructor accepted null for second argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int) constructor threw an unexpected " +
                                 "exception when given null for second argument", t);
        }        
        try {
            Service<SimpleInterface> badService = new Service<SimpleInterface>(SimpleInterface.class, null, port, false, false);
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor accepted null for second argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor threw an unexpected " +
                                 "exception when given null for second argument", t);
        }


        /** Ensures that <code>Service</code> constructors accept suitable parameters without exception/error. */
        try {
            Service<SimpleInterface> service = new Service<SimpleInterface>(SimpleInterface.class, new SimpleObject(), port);
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int) constructor threw an unexpected exception", t);
        }        
        try {
            Service<SimpleInterface> service = new Service<SimpleInterface>(SimpleInterface.class, new SimpleObject(), port, false, false);
        } catch(Throwable t) {
            throw new TestFailed("Service(Class<T>, T, int, boolean, boolean) constructor threw an unexpected exception", t);
        }
    }

    /** Object that implements <code>BadInterface</code>. */
    private class BadObject implements BadInterface {
        /** Returns its argument. */
        @Override
        public int method(int argument) {
            return argument;
        }
    }
}
