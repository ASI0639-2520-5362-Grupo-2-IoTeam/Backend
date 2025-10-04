package pe.iotteam.plantcare.plant.interfaces.rest.resources;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdatePlantResource {
    private String name;
    private String type;
    private String imgUrl;
    private String bio;
    private String location;
}