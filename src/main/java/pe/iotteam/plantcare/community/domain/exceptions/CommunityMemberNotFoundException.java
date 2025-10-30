package pe.iotteam.plantcare.community.domain.exceptions;

public class CommunityMemberNotFoundException extends RuntimeException {
    public CommunityMemberNotFoundException(String message) {
        super(message);
    }
}
