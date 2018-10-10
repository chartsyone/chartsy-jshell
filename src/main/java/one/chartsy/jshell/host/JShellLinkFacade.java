package one.chartsy.jshell.host;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import jdk.jshell.execution.DirectExecutionControl;
import one.chartsy.jshell.AccessDeniedException;
import one.chartsy.jshell.JShellLink;

/**
 * Provides a host-side implementation of the {@code JShellLink} service
 * interface.
 * <p>
 * The {@code JShellLinkFacade} is a remote object that need to be bound on the
 * host side to the RMI registry. The host side may use
 * {@link JShellLink#register(one.chartsy.jshell.JShellLink, java.rmi.registry.Registry, boolean)
 * register} method for that purpose.
 *
 * @author Mariusz Bernacki
 */
public class JShellLinkFacade extends UnicastRemoteObject implements JShellLink {
    /** The serial version UID. */
    private static final long serialVersionUID = 3180023413968413087L;
    /** The port number on which to export this object and all created session objects. */
    private final int port;

    /**
     * Creates and exports a new facade object using an anonymous port.
     * 
     * @throws RemoteException if failed to export object
     */
    public JShellLinkFacade() throws RemoteException {
        this(0);
    }

    /**
     * Creates and exports a new facade object using the specified port.
     * 
     * @param port the port number on which to export this object and all subsequently
     * created client session objects
     * @throws RemoteException if failed to export object
     */
    public JShellLinkFacade(int port) throws RemoteException {
        super(port);
        this.port = port;
    }
    
    @Override
    public Session createSession(String accessKey) throws AccessDeniedException, RemoteException {
        try {
            String hash = System.getProperty("chartsy.jshell.secret.hash");
            if (hash != null && !hash.isEmpty()) {
                // The host has secret hash, thus require access key from the caller
                if (accessKey == null || accessKey.isEmpty())
                    throw new AccessDeniedException("ACCESS DENIED (Missing `secret` key)");
                if (!PBKDF2.computeToString(accessKey.toCharArray()).equals(hash))
                    throw new AccessDeniedException("ACCESS DENIED (Invalid `secret` key)");
            }
            return createSession();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RemoteException("accessKey hash calculation failed due to " + e, e);
        }
    }
    
    /**
     * Makes a new client session object assuming the caller is already authorized.
     * 
     * @return the JShell client session object (remote)
     * @throws RemoteException if failed to export object
     */
    protected Session createSession() throws RemoteException {
        return new JShellSessionObject(new DirectExecutionControl(), port);
    }
}
