package pe.iotteam.plantcare.auth.interfaces.rest.transform;


import pe.iotteam.plantcare.auth.domain.model.commands.GoogleLoginCommand;
import pe.iotteam.plantcare.auth.interfaces.rest.resources.GoogleSignInResource;

public class GoogleSignInCommandFromResourceAssembler {

    public static GoogleLoginCommand toCommandFromResource(GoogleSignInResource resource) {
        return new GoogleLoginCommand(resource.googleToken());
    }
}
