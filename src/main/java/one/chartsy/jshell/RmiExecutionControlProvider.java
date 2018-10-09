package one.chartsy.jshell;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map;
import jdk.jshell.spi.ExecutionControl;
import jdk.jshell.spi.ExecutionControlProvider;
import jdk.jshell.spi.ExecutionEnv;

/**
 * A provider of remote, RMI-based execution engines.
 *
 * @author Mariusz Bernacki
 */
public class RmiExecutionControlProvider implements ExecutionControlProvider {
    /** The RMI service (registry) hostname to connect to. */
    public static final String PARAM_HOST = "host";
    /** The RMI service (registry) port to connect to. */
    public static final String PARAM_PORT = "port";
    /** The secret access key used as proof of authentication. */
    public static final String PARAM_SECRET_KEY = "secret";

    /**
     * The unique name of this {@code ExecutionControlProvider}.
     *
     * @return "rmi"
     */
    @Override
    public String name() {
        return "rmi";
    }

    @Override
    public Map<String, String> defaultParameters() {
        return Map.of(PARAM_HOST, "localhost", PARAM_PORT, String.valueOf(Registry.REGISTRY_PORT), PARAM_SECRET_KEY,
                "");
    }

    @Override
    public ExecutionControl generate(ExecutionEnv env, Map<String, String> parameters) throws Throwable {
        // Get the parameter's default values
        Map<String, String> defaults = defaultParameters();
        if (parameters == null)
            parameters = defaults;

        // Obtain the 'host' parameter
        String host = parameters.getOrDefault(PARAM_HOST, defaults.get(PARAM_HOST));
        // Obtain the 'port' parameter
        int port;
        try {
            port = Integer.parseInt(parameters.getOrDefault(PARAM_PORT, defaults.get(PARAM_PORT)));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("The `" + PARAM_PORT + "` parameter is invalid: " + e.getMessage());
        }

        // Create the RMI-based execution control
        Registry registry = LocateRegistry.getRegistry(host, port);
        JShellLink jshell = (JShellLink) registry.lookup("JShell");
        String accessKey = parameters.get(PARAM_SECRET_KEY);

        return new RmiExecutionControl(jshell.createSession(accessKey));
    }
}
