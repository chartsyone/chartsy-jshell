package one.chartsy.jshell;

/**
 * Specifies a generic remote access exception.
 *
 * @author Mariusz Bernacki
 */
public class RemoteAccessException extends RuntimeException {

    private static final long serialVersionUID = -1718403859906310666L;

    /**
     * Constructs an instance of <code>RemoteAccessException</code> with the
     * specified detail message.
     *
     * @param msg
     *            the detail message.
     */
    public RemoteAccessException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>RemoteAccessException</code> with the
     * specified detail message.
     *
     * @param msg
     *            the detail message.
     * @param x
     *            the nested exception
     */
    public RemoteAccessException(String msg, Throwable x) {
        super(msg, x);
    }

    /**
     * Constructs an instance of <code>RemoteAccessException</code> with the
     * specified detail message.
     *
     * @param x
     *            the nested exception
     */
    public RemoteAccessException(Throwable x) {
        super(x.toString(), x);
    }
}
