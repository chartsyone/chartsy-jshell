package one.chartsy.jshell.host;

import java.rmi.registry.LocateRegistry;
import one.chartsy.jshell.JShellLink;

/**
 * Main class for launching a test server.
 *
 * @author Mariusz Bernacki
 */
public class Main {
    
    public static void main(String[] args) throws Exception {
        System.setProperty("chartsy.jshell.secret.hash", "22lEL0T36LRs313UEzoHg5iqwvV5rRqTzL4JfG4tbdc=");
        int port = 52099;
        JShellLink.register(new JShellLinkFacade(port), LocateRegistry.createRegistry(port), false);
    }
}
