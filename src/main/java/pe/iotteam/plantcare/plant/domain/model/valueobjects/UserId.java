package pe.iotteam.plantcare.plant.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import java.util.UUID;

/**
 * Represents a User's unique identifier as a Value Object.
 * Using a record simplifies the implementation, making it immutable and concise.
 */
@Embeddable
public record UserId(UUID value) {

    /**
     * Compact constructor to enforce validation rules.
     * Ensures the underlying UUID value is never null.
     */
    public UserId {
        Objects.requireNonNull(value, "The user ID value cannot be null.");
    }

    /**
     * Convenience constructor to create a UserId from a String.
     * @param userId The string representation of the UUID.
     */
    public UserId(String userId) {
        this(UUID.fromString(userId));
    }

    /**
     * Overrides the default record toString() to return only the UUID value as a String.
     * This can be useful for serialization or logging.
     * @return The string representation of the UUID.
     */
    @Override
    public String toString() {
        return value.toString();
    }
}