package one.chartsy.jshell;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import jdk.jshell.spi.ExecutionControl;

import static jdk.jshell.spi.ExecutionControl.*;

/**
 * This interface specifies the remote service interface for the JShell
 * execution engine running on the host server.
 * <p>
 * The methods exposed by this interface mimics the the methods required by the
 * {@link ExecutionControl} interface.
 *
 * @author Mariusz Bernacki
 */
public interface JShellLink extends Remote {

    /**
     * Creates a new JShell session on the host-side.
     * 
     * @param accessKey
     *            the secret access key that provides proof of authentication
     * @return
     * @throws AccessDeniedException
     * @throws RemoteException
     */
    Session createSession(String accessKey) throws AccessDeniedException, RemoteException;

    /**
     * Registers (binds) the given link instance in the provided RMI registry.
     * <p>
     * The given link {@code instance} must be already exported by either
     * subclassing {@link UnicastRemoteObject} or by calling
     * {@code UnicastRemoteObject.exportObject) first on the instance. The instances
     * of {@link JShellLinkFacade} are already exported when created.
     * 
     * @param instance the instance to register
     * 
     * @param registry
     *            the RMI registry
     * @param rebind
     * @throws RemoteException
     * @throws AlreadyBoundException
     */
    public static void register(JShellLink instance, Registry registry, boolean rebind)
            throws RemoteException, AlreadyBoundException {
        if (rebind)
            registry.rebind("JShell", instance);
        else
            registry.bind("JShell", instance);
    }

    /**
     * The single-user session of JShell conversation between the client and the
     * server.
     * 
     */
    public interface Session extends Remote {

        /**
         * Attempts to load new classes.
         *
         * @param cbcs
         *            the class name and bytecodes to load
         * @throws ClassInstallException
         *             exception occurred loading the classes, some or all were not
         *             loaded
         * @throws NotImplementedException
         *             if not implemented
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws RemoteException
         *             if a remote access error occurred
         */
        void load(ClassBytecodes[] cbcs)
                throws ClassInstallException, NotImplementedException, EngineTerminationException, RemoteException;

        /**
         * Attempts to redefine previously loaded classes.
         *
         * @param cbcs
         *            the class name and bytecodes to redefine
         * @throws ClassInstallException
         *             exception occurred redefining the classes, some or all were not
         *             redefined
         * @throws NotImplementedException
         *             if not implemented
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws RemoteException
         *             if a remote access error occurred
         */
        void redefine(ClassBytecodes[] cbcs)
                throws ClassInstallException, NotImplementedException, EngineTerminationException, RemoteException;

        /**
         * Invokes an executable Snippet by calling a method on the specified wrapper
         * class. The method must have no arguments and return String.
         *
         * @param className
         *            the class whose method should be invoked
         * @param methodName
         *            the name of method to invoke
         * @return the result of the execution or null if no result
         * @throws UserException
         *             the invoke raised a user exception
         * @throws ResolutionException
         *             the invoke attempted to directly or indirectly invoke an
         *             unresolved snippet
         * @throws StoppedException
         *             if the {@code invoke()} was canceled by
         *             {@link ExecutionControl#stop}
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws InternalException
         *             an internal problem occurred
         * @throws RemoteException
         *             if a remote access error occurred
         */
        String invoke(String className, String methodName)
                throws RunException, EngineTerminationException, InternalException, RemoteException;

        /**
         * Returns the value of a variable.
         *
         * @param className
         *            the name of the wrapper class of the variable
         * @param varName
         *            the name of the variable
         * @return the value of the variable
         * @throws UserException
         *             formatting the value raised a user exception
         * @throws ResolutionException
         *             formatting the value attempted to directly or indirectly invoke
         *             an unresolved snippet
         * @throws StoppedException
         *             if the formatting the value was canceled by
         *             {@link ExecutionControl#stop}
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws InternalException
         *             an internal problem occurred
         * @throws RemoteException
         *             if a remote access error occurred
         */
        String varValue(String className, String varName)
                throws RunException, EngineTerminationException, InternalException, RemoteException;

        /**
         * Adds the path to the execution class path.
         *
         * @param path
         *            the path to add
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws InternalException
         *             an internal problem occurred
         * @throws RemoteException
         *             if a remote access error occurred
         */
        void addToClasspath(String path) throws EngineTerminationException, InternalException, RemoteException;

        /**
         * Interrupts a running invoke.
         *
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws InternalException
         *             an internal problem occurred
         * @throws RemoteException
         *             if a remote access error occurred
         */
        void stop() throws EngineTerminationException, InternalException, RemoteException;

        /**
         * Run a non-standard command (or a standard command from a newer version).
         *
         * @param command
         *            the non-standard command
         * @param arg
         *            the commands argument
         * @return the commands return value
         * @throws UserException
         *             the command raised a user exception
         * @throws ResolutionException
         *             the command attempted to directly or indirectly invoke an
         *             unresolved snippet
         * @throws StoppedException
         *             if the command was canceled by {@link ExecutionControl#stop}
         * @throws EngineTerminationException
         *             the execution engine has terminated
         * @throws NotImplementedException
         *             if not implemented
         * @throws InternalException
         *             an internal problem occurred
         * @throws RemoteException
         *             if a remote access error occurred
         */
        Object extensionCommand(String command, Object arg)
                throws RunException, EngineTerminationException, InternalException, RemoteException;

        /**
         * Shuts down this execution engine. Implementation should free all resources
         * held by this execution engine.
         * <p>
         * No calls to methods on this interface should be made after close.
         * 
         * @throws RemoteException
         *             if a remote access error occurred
         */
        void close() throws RemoteException;
    }
}
