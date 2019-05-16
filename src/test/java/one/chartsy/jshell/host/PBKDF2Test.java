package one.chartsy.jshell.host;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.GeneralSecurityException;

import org.junit.jupiter.api.Test;


public class PBKDF2Test {
    
    @Test
    void computeToString_gives_expected_PBKDF2() throws GeneralSecurityException {
        String input = "password123";
        String expected = "22lEL0T36LRs313UEzoHg5iqwvV5rRqTzL4JfG4tbdc=";
        
        String actual = PBKDF2.computeToString(input.toCharArray());
        assertEquals(expected, actual);
    }
}
