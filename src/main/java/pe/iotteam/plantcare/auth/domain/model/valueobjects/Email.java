package pe.iotteam.plantcare.auth.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Email(String value) {
    public Email {
        if (value == null || !value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }
}