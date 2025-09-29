package pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.mappers;

import pe.iotteam.plantcare.auth.domain.model.aggregates.UserAccount;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.Email;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.HashedPassword;
import pe.iotteam.plantcare.auth.domain.model.valueobjects.UserId;

public class UserMapper {

    public static UserEntity toEntity(UserAccount user) {
        return new UserEntity(
                user.getUserId().value(),
                user.getEmail().value(),
                user.getHashedPassword().value(),
                user.getRole()
        );
    }

    public static UserAccount toAggregate(UserEntity entity) {
        return new UserAccount(
                new UserId(entity.getId()),
                new Email(entity.getEmail()),
                new HashedPassword(entity.getPassword()),
                entity.getRole()
        );
    }
}