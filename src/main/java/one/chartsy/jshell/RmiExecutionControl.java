package one.chartsy.jshell;

import java.rmi.RemoteException;
import java.util.Objects;
import jdk.jshell.spi.ExecutionControl;

/**
 * An implementation of the JShell execution engine based on the RMI for
 * communication with the target VM.
 *
 * @author Mariusz Bernacki
 */
public final class RmiExecutionControl implements ExecutionControl {
    /** The JShell link session used for communication with the target VM. */
    private final JShellLink.Session session;

    /**
     * Constructs a new RMI-based execution engine.
     * 
     * @param session
     *            the JShell link session used for communication with the target VM
     */
    protected RmiExecutionControl(JShellLink.Session session) {
        this.session = Objects.requireNonNull(session, "session");
    }

    @Override
    public void load(ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        try {
            session.load(cbcs);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public void redefine(ClassBytecodes[] cbcs)
            throws ClassInstallException, NotImplementedException, EngineTerminationException {
        try {
            session.redefine(cbcs);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public String invoke(String className, String methodName)
            throws RunException, EngineTerminationException, InternalException {
        try {
            return session.invoke(className, methodName);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public String varValue(String className, String varName)
            throws RunException, EngineTerminationException, InternalException {
        try {
            return session.varValue(className, varName);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public void addToClasspath(String path) throws EngineTerminationException, InternalException {
        try {
            session.addToClasspath(path);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public void stop() throws EngineTerminationException, InternalException {
        try {
            session.stop();
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public Object extensionCommand(String command, Object arg)
            throws RunException, EngineTerminationException, InternalException {
        try {
            return session.extensionCommand(command, arg);
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }

    @Override
    public void close() {
        try {
            session.close();
        } catch (RemoteException e) {
            throw new RemoteAccessException(e);
        }
    }
}
