package pe.iotteam.plantcare.auth.interfaces.rest.resources;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    private String id;
    private String email;
    private String username;
    private String role;

    public UserDto() {}
    public UserDto(String id, String email, String username, String role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.role = role;
    }

}
