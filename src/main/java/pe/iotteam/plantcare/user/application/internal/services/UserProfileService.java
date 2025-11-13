package pe.iotteam.plantcare.user.application.internal.services;

import org.springframework.web.multipart.MultipartFile;
import pe.iotteam.plantcare.user.interfaces.rest.resources.*;

import java.util.List;
import java.util.UUID;

public interface UserProfileService {
    UserProfileResource getProfileByEmail(String email);

    UserProfileResource updateProfileByEmail(String email, UpdateProfileRequest request);

    String uploadAvatarByEmail(String email, MultipartFile file) throws Exception;

    UserStatsResource getStatsByEmail(String email);

    AchievementsResponse getAchievementsByEmail(String email);
}
