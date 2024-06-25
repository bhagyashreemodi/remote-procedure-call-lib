package test.remote;

/** Non-remote interface that remote constructors should reject.

    <p>
    This interface is used in multiple tests.
 */
public interface BadInterface
{
    /** Causes the interface to be rejected for use in remote.

        <p>
        This public method is not declared as throwing
        <code>RemoteObjectException</code>, and therefore this interface is not
        considered to be a remote interface.
     */
    public int method(int argument) throws java.io.FileNotFoundException;
}
