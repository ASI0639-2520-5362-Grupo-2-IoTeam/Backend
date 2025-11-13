package pe.iotteam.plantcare.user.interfaces.rest.resources;

import jakarta.validation.constraints.Size;

public class UpdateProfileRequest {

    @Size(max = 200)
    private String fullName;

    @Size(max = 30)
    private String phone;

    @Size(max = 1000)
    private String bio;

    @Size(max = 200)
    private String location;

    public UpdateProfileRequest() {}

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getBio() { return bio; }
    public void setBio(String bio) { this.bio = bio; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
