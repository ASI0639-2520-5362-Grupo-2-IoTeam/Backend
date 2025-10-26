package pe.iotteam.plantcare.auth.infrastructure.oauth.google.services;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

/**
 * Google OAuth Service
 * This service handles Google OAuth token verification
 */

@Service
public class GoogleOAuthService {
    @Value("${google.oauth2.client-id}")
    private String clientId;

    private GoogleIdTokenVerifier verifier;

    @PostConstruct
    public void init() {
        System.out.println("Loaded Google client ID: " + clientId); // debug
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleIdToken.Payload verifyToken(String tokenString) throws Exception {
        GoogleIdToken idToken = verifier.verify(tokenString);
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            if (!payload.getEmailVerified()) {
                throw new RuntimeException("Google email not verified");
            }

            return payload;
        }
        throw new RuntimeException("Invalid Google token");
    }

    /**
     * Extracts user information from Google token payload
     * @param payload The Google token payload
     * @return GoogleUserInfo object containing user details
     */
    public GoogleUserInfo extractUserInfo(GoogleIdToken.Payload payload) {
        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String givenName = (String) payload.get("given_name");
        String familyName = (String) payload.get("family_name");
        String picture = (String) payload.get("picture");

        return new GoogleUserInfo(email, name, givenName, familyName, picture);
    }

    /**
     * Inner class to represent Google user information
     */
    public static class GoogleUserInfo {
        private final String email;
        private final String name;
        private final String givenName;
        private final String familyName;
        private final String picture;

        public GoogleUserInfo(String email, String name, String givenName, String familyName, String picture) {
            this.email = email;
            this.name = name;
            this.givenName = givenName;
            this.familyName = familyName;
            this.picture = picture;
        }

        // Getters
        public String getEmail() { return email; }
        public String getName() { return name; }
        public String getGivenName() { return givenName; }
        public String getFamilyName() { return familyName; }
        public String getPicture() { return picture; }
    }
}
