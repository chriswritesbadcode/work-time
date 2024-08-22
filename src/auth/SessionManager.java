package auth;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

public class SessionManager {
    private static final String SESSION_FILE = "session.dat";
    private static final int SESSION_VALIDITY_DAYS = 2;

    private static String generateSessionToken() {
        SecureRandom random = new SecureRandom();
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);

        return Base64.getEncoder().encodeToString(tokenBytes);
    }

    public static String[] createSession() throws IOException {
        String token = generateSessionToken();
        long expirationTime = new Date().getTime() + (SESSION_VALIDITY_DAYS * 24 * 60 * 60 * 1000L);

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(SESSION_FILE))) {
            writer.write(token + '\n');
            writer.write(Long.toString(expirationTime));
        }

        return new String[] { token, Long.toString(expirationTime) };
    }

    public static boolean validateSession() throws IOException {
        if (!Files.exists(Paths.get(SESSION_FILE))) {
            return false;
        }

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(SESSION_FILE))) {
            String token = reader.readLine();
            long expirationTime = Long.parseLong(reader.readLine());

            if (new Date().getTime() > expirationTime) {
                return false;
            }

            return true;
        }
    }

    public static void invalidateSession() throws IOException {
        Files.deleteIfExists(Paths.get(SESSION_FILE));
    }
}
