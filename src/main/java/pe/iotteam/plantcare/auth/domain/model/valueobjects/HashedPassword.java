package pe.iotteam.plantcare.auth.domain.model.valueobjects;

public record HashedPassword(String value) {
    public HashedPassword {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be empty");
        }
    }
}