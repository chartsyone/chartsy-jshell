package one.chartsy.jshell.host;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Objects;
import jdk.jshell.execution.DirectExecutionControl;
import jdk.jshell.spi.ExecutionControl;
import one.chartsy.jshell.JShellLink;

import static jdk.jshell.spi.ExecutionControl.*;

/**
 * A default implementation of a {@code JShellLink.Session}.
 *
 * @author Mariusz Bernacki
 */
public class JShellSessionObject extends UnicastRemoteObject implements JShellLink.Session {
    /** The serial version UID. */
    private static final long serialVersionUID = -8601498605988180882L;
    /** The execution engine to which the remotely received commands are send. */
    private final ExecutionControl engine;

    /**
     * Creates and exports a new JShell session using an anonymous port. The created
     * session uses the {@link DirectExecutionControl} for JShell snippets
     * execution.
     *
     * @throws RemoteException
     *             if failed to export object
     */
    public JShellSessionObject() throws RemoteException {
        this(new DirectExecutionControl());
    }

    /**
     * Creates and exports a new JShell session using the specified port. The
     * created session uses the {@link DirectExecutionControl} for JShell snippets
     * execution.
     *
     * @param port
     *            the port on which to export the session object
     * @throws RemoteException
     *             if failed to export object
     */
    public JShellSessionObject(int port) throws RemoteException {
        this(new DirectExecutionControl(), port);
    }

    /**
     * Creates and exports a new JShell session using an anonymous port. The created
     * session uses the specified engine for JShell snippets execution.
     *
     * @param engine
     *            the execution exgine to use
     * @throws RemoteException
     *             if failed to export object through RMI
     */
    public JShellSessionObject(ExecutionControl engine) throws RemoteException {
        this(engine, 0);
    }

    /**
     * Creates and exports a new JShell session using the specified port. The
     * created session uses the specified engine for JShell snippets execution.
     *
     * @param engine
     *            the execution exgine to use
     * @param port
     *            the port on which to export the session object
     * @throws RemoteException
     *             if failed to export object through RMI
     */
    public JShellSessionObject(ExecutionControl engine, int port) throws RemoteException {
        super(port);
        this.engine = Objects.requireNonNull(engine, "engine");
    }

    @Override
    public void load(ExecutionControl.ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        engine.load(cbcs);
    }

    @Override
    public void redefine(ExecutionControl.ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        engine.redefine(cbcs);
    }

    @Override
    public String invoke(String className, String methodName)
            throws RunException, EngineTerminationException, InternalException {
        return engine.invoke(className, methodName);
    }

    @Override
    public String varValue(String className, String varName)
            throws RunException, EngineTerminationException, InternalException {
        return engine.varValue(className, varName);
    }

    @Override
    public void addToClasspath(String path) throws EngineTerminationException, InternalException {
        engine.addToClasspath(path);
    }

    @Override
    public void stop() throws EngineTerminationException, InternalException {
        engine.stop();
    }

    @Override
    public Object extensionCommand(String command, Object arg)
            throws RunException, EngineTerminationException, InternalException {
        return engine.extensionCommand(command, arg);
    }

    @Override
    public void close() {
        engine.close();
    }
}
