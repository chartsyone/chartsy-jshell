package one.chartsy.jshell.host;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.rmi.RemoteException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import one.chartsy.jshell.AccessDeniedException;
import one.chartsy.jshell.JShellLink.Session;


public class JShellLinkFacadeTest {
    
    private JShellLinkFacade facade;
    
    @BeforeEach
    void createFacadeObject() throws RemoteException {
        facade = new JShellLinkFacade();
    }
    
    @BeforeEach
    void setSecretKeyProperty() {
        System.setProperty("chartsy.jshell.secret.hash", "22lEL0T36LRs313UEzoHg5iqwvV5rRqTzL4JfG4tbdc=");
    }
    
    @AfterEach
    void clearSecretKeyProperty() {
        System.clearProperty("chartsy.jshell.secret.hash");
    }
    
    @Test
    void createSession_throws_AccessDeniedException_when_given_missing_accessKey() {
        assertThrows(AccessDeniedException.class, () -> facade.createSession(null));
        assertThrows(AccessDeniedException.class, () -> facade.createSession(""));
    }
    
    @Test
    void createSession_throws_AccessDeniedException_when_given_invalid_accessKey() {
        assertThrows(AccessDeniedException.class, () -> facade.createSession("invalid_accessKey"));
    }
    
    @Test
    void createSession_creates_Session_when_given_valid_accessKey() throws Exception {
        Session result = facade.createSession("password123");
        
        assertNotNull(result);
        result.close();
    }
}
