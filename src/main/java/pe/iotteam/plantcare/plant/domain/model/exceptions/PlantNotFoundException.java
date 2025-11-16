package pe.iotteam.plantcare.plant.domain.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlantNotFoundException extends RuntimeException {
    public PlantNotFoundException(Long plantId) {
        super("Plant with id " + plantId + " not found");
    }
}
