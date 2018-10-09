package one.chartsy.jshell.host;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Pbkdf2-based password hash generator used by the library.
 *
 * @author Mariusz Bernacki
 */
public class PBKDF2 {

    public static String computeToString(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return Base64.getEncoder().encodeToString(compute(password));
    }

    public static byte[] compute(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        String salt = "chartsy1";
        int iterations = 10_000;
        int keyLength = 32;

        KeySpec spec = new PBEKeySpec(password, salt.getBytes(StandardCharsets.ISO_8859_1), iterations, keyLength * 8);
        return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(spec).getEncoded();
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println();
            System.out.println("Pbkdf2-based password hash generator used by the library.");
            System.out.println("Usage example:");
            System.out.println();
            System.out.println("\tjava -cp ./lib/chartsy-jshell.jar " + PBKDF2.class.getName() + " <secret key>");
            System.out.println();
            return;
        }

        System.out.println();
        System.out.println("            Client Secret Key: " + args[0]);
        System.out.println("Corresponding");
        System.out.println("   chartsy.jshell.secret.hash: " + computeToString(args[0].toCharArray()));
    }
}
