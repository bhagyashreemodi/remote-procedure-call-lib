package test.remote;

import remote.*;
import java.io.FileNotFoundException;

/** Simple implementation of <code>SimpleInterface</code>.

    <p>
    This class is used in multiple tests.
 */
public class SimpleObject implements SimpleInterface {
    /** The sleeping thread does not return until this becomes <code>false</code>. */
    private boolean sleeping = true;
    /** If <code>true</code>, the next thread to call <code>rendezvous</code>
        should wake all sleeping threads. */
    private boolean wake = false;

    // Methods documented in SimpleInterface.java.
    @Override
    public Object method(boolean throw_exception)
        throws RemoteObjectException, FileNotFoundException {
        if(throw_exception)
            throw new FileNotFoundException();
        else
            return null;
    }

    @Override
    public synchronized void rendezvous() throws RemoteObjectException {
        // If wake is false, this thread should go to sleep. If it is true,
        // this thread should wake the sleeping thread.
        if(!wake) {
            wake = true;

            while(sleeping) {
                try {
                    wait();
                } catch(InterruptedException e) { }
            }
        } else {
            sleeping = false;
            notifyAll();
        }
    }

    /** Wakes all sleeping receiving threads. */
    public synchronized void wake() {
        sleeping = false;
        notifyAll();
    }
}
