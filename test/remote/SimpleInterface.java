package test.remote;

import remote.*;
import java.io.FileNotFoundException;

/** Simple interface for a remote service.

    <p>
    This interface is used in multiple tests.
 */
public interface SimpleInterface
{
    /** Tests transmission of arguments and returning of results.

        @param throw_exception If <code>true</code>, this method throws
                               <code>FileNotFoundException</code>. Otherwise, it
                               returns <code>null</code>.
        @return <code>null</code>.
        @throws FileNotFoundException If the argument is <code>true</code>.
        @throws RemoteObjectException If the call cannot be complete due to a network
                             error.
     */
    public Object method(boolean throw_exception)
        throws RemoteObjectException, FileNotFoundException;

    /** Permits two threads to rendezvous with control inside the service.

        <p>
        The first thread to call this method blocks until a second method wakes
        it.

        @throws RemoteObjectException If the call cannot be complete due to a network
                             error.
     */
    public void rendezvous() throws RemoteObjectException;
}
