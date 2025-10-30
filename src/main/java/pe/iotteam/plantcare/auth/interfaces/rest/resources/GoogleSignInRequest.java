package pe.iotteam.plantcare.auth.interfaces.rest.resources;

import jakarta.validation.constraints.NotBlank;

public class GoogleSignInRequest {
    @NotBlank
    private String idToken;

    public GoogleSignInRequest() {}
    public GoogleSignInRequest(String idToken) { this.idToken = idToken; }

    public String getIdToken() { return idToken; }
    public void setIdToken(String idToken) { this.idToken = idToken; }
}
