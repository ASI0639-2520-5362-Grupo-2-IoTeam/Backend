package pe.iotteam.plantcare.auth.domain.model.entities;

public class Role {
    private final String name;

    public Role(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name cannot be empty");
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}