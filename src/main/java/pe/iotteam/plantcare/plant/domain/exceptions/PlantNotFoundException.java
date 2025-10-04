package pe.iotteam.plantcare.plant.domain.exceptions;

public class PlantNotFoundException extends RuntimeException {
    public PlantNotFoundException(String message) {
        super(message);
    }
}