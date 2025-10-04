package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class CreatePlantResource {
    private UUID userId;
    private String name;
    private String type;
    private String imgUrl;
    private String bio;
    private String location;
}