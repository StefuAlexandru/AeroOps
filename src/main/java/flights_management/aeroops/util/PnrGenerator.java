package flights_management.aeroops.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class PnrGenerator {
    private static final String ALPHANUM = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RND = new SecureRandom();

    public String generate() {
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) sb.append(ALPHANUM.charAt(RND.nextInt(ALPHANUM.length())));
        return sb.toString();
    }
}