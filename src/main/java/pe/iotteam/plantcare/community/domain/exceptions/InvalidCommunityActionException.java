package pe.iotteam.plantcare.community.domain.exceptions;

public class InvalidCommunityActionException extends RuntimeException {
    public InvalidCommunityActionException(String message) {
        super(message);
    }
}