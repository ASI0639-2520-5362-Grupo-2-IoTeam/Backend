package pe.iotteam.plantcare.subscription.domain.exceptions;

public class InvalidSubscriptionStateException extends RuntimeException {
    public InvalidSubscriptionStateException(String message) {
        super(message);
    }
}
