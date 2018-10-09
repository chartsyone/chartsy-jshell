package one.chartsy.jshell;

/**
 * Thrown by JShellLink to indicate a not authorized attempt to access the
 * resource.
 *
 * @author Mariusz Bernacki
 */
public class AccessDeniedException extends Exception {

    private static final long serialVersionUID = -392499355299152827L;

    /**
     * Constructs a new exception with the specified message.
     * 
     * @param message
     *            the message
     */
    public AccessDeniedException(String message) {
        super(message);
    }
}
