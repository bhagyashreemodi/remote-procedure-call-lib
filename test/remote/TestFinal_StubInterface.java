package test.remote;

import test.util.*;
import remote.*;
import java.net.*;
import java.util.Random;

/** Performs basic tests on the public interface of {@link remote.StubFactory}.

    <p>
    The test verifies that both <code>StubFactory.create()</code> methods:
    <ul>
    <li> rejects classes,
    <li> rejects non-remote interfaces,
    <li> rejects null arguments,
    <li> accepts remote interfaces.
    </ul>
 */
public class TestFinal_StubInterface extends Test {
    /** Test notice. */
    public static final String notice = 
        "checking requirements of stub creation arguments";
    /** Service address used for the creation of stubs. */
    private String address;

    /** Initializes the test. */
    @Override
    protected void initialize() throws TestFailed {
        Random rng = new Random(System.nanoTime());
        int port = rng.nextInt(10000) + 7000;

        address = "127.0.0.1:" + Integer.toString(port);
    }

    /** Performs the test. */
    @Override
    protected void perform() throws TestFailed {
        /** Ensures that a <code>Stub</code> cannot be created from a class. */
        try {
            Object stub = StubFactory.create(Object.class, address);
            throw new TestFailed("StubFactory.create(Class<T>, String) accepted a class");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String) threw an unexpected " +
                                 "exception when given a class", t);
        }
        try {
            Object stub = StubFactory.create(Object.class, address, false, false);
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) accepted a class");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) threw an unexpected " +
                                 "exception when given a class", t);
        }
        
        /** Ensures that a <code>Stub</code> cannot be created from a non-remote interface. */
        try {
            BadInterface stub = StubFactory.create(BadInterface.class, address);
            throw new TestFailed("StubFactory.create(Class<T>, String) accepted a non-remote interface");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String) threw an unexpected " +
                                 "exception when given a non-remote interface", t);
        }
        try {
            BadInterface stub = StubFactory.create(BadInterface.class, address, false, false);
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) accepted a non-remote interface");
        } catch(Error e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) threw an unexpected " + 
                                 "exception when given a non-remote interface", t);
        }

        /** Ensures that both <code>StubFactory.create</code> methods throw <code>NullPointerException</code>
            when given <code>null</code> for any parameters. */
        // Make sure that null for the first argument is rejected.
        try {
            SimpleInterface stub = StubFactory.create(null, address);
            throw new TestFailed("StubFactory.create(Class<T>, String) accepted null for first argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String) threw an unexpected " + 
                                 "exception when given null for first argument", t);
        }
        try {
            SimpleInterface stub = StubFactory.create(null, address, false, false);
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) accepted null for first argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) threw an unexpected " + 
                                 "exception when given null for first argument", t);
        }
        // Make sure that null for the second argument is rejected.
        try {
            SimpleInterface stub = StubFactory.create(SimpleInterface.class, (String)null);
            throw new TestFailed("StubFactory.create(Class<T>, String) accepted null for second argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String) threw an unexpected " + 
                                 "exception when given null for second argument", t);
        }
        try {
            SimpleInterface stub = StubFactory.create(SimpleInterface.class, (String)null, false, false);
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) accepted null for second argument");
        } catch(NullPointerException e) { 
        } catch(Throwable t) {
            throw new TestFailed("StubFactory.create(Class<T>, String, boolean, boolean) threw an unexpected " + 
                                 "exception when given null for second argument", t);
        }
    }
}

