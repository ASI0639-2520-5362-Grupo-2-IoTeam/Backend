package pe.iotteam.plantcare.user.application.internal.services.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pe.iotteam.plantcare.auth.domain.model.entities.UserEntity;
import pe.iotteam.plantcare.auth.infrastructure.persistence.jpa.repositories.UserRepositoryJpa;
import pe.iotteam.plantcare.plant.infrastructure.persistence.jpa.repositories.PlantJpaRepository;
import pe.iotteam.plantcare.user.application.internal.services.UserProfileService;
import pe.iotteam.plantcare.user.domain.model.entities.AchievementEntity;
import pe.iotteam.plantcare.user.domain.model.entities.UserProfileEntity;
import pe.iotteam.plantcare.user.infrastructure.persistence.jpa.repositories.AchievementRepository;
import pe.iotteam.plantcare.user.infrastructure.persistence.jpa.repositories.UserProfileRepository;
import pe.iotteam.plantcare.user.infrastructure.storage.AvatarStorageService;
import pe.iotteam.plantcare.user.interfaces.rest.resources.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepositoryJpa userRepository;
    private final UserProfileRepository profileRepository;
    private final PlantJpaRepository plantRepository;
    private final AchievementRepository achievementRepository;
    private final AvatarStorageService avatarStorageService;

    public UserProfileServiceImpl(UserRepositoryJpa userRepository, UserProfileRepository profileRepository,
                                  PlantJpaRepository plantRepository, AchievementRepository achievementRepository,
                                  AvatarStorageService avatarStorageService) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.plantRepository = plantRepository;
        this.achievementRepository = achievementRepository;
        this.avatarStorageService = avatarStorageService;
    }

    private UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public UserProfileResource getProfileByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        UUID userId = user.getId();
        UserProfileEntity profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> createDefaultProfileForUser(user));

        var stats = computeStats(userId.toString());

        return new UserProfileResource(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getBio(),
                profile.getLocation(),
                profile.getAvatarUrl(),
                profile.getJoinDate(),
                profile.getLastLogin(),
                stats
        );
    }

    private UserProfileEntity createDefaultProfileForUser(UserEntity user) {
        UserProfileEntity p = new UserProfileEntity();
        p.setUser(user);
        p.setJoinDate(user.getCreatedAt());
        p.setLastLogin(user.getUpdatedAt() != null ? user.getUpdatedAt() : LocalDateTime.now());
        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return profileRepository.save(p);
    }

    private UserStatsResource computeStats(String userId) {
        var plants = plantRepository.findByUserId(userId);
        long totalPlants = plants.size();
        long wateringSessions = plants.stream().mapToLong(p -> p.getWateringLogs() == null ? 0 : p.getWateringLogs().size()).sum();
        long alive = plants.stream().filter(p -> "active".equalsIgnoreCase(p.getStatus()) || "alive".equalsIgnoreCase(p.getStatus())).count();
        int successRate = totalPlants == 0 ? 0 : (int) ((alive * 100) / totalPlants);
        return new UserStatsResource(totalPlants, wateringSessions, successRate);
    }

    @Override
    public UserProfileResource updateProfileByEmail(String email, UpdateProfileRequest request) {
        UserEntity user = findUserByEmail(email);
        UUID userId = user.getId();
        UserProfileEntity profile = profileRepository.findByUserId(userId).orElseGet(() -> createDefaultProfileForUser(user));

        if (request.getFullName() != null) profile.setFullName(request.getFullName());
        if (request.getPhone() != null) profile.setPhone(request.getPhone());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getLocation() != null) profile.setLocation(request.getLocation());
        profile.setUpdatedAt(LocalDateTime.now());
        profileRepository.save(profile);

        var stats = computeStats(userId.toString());

        return new UserProfileResource(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                profile.getFullName(),
                profile.getPhone(),
                profile.getBio(),
                profile.getLocation(),
                profile.getAvatarUrl(),
                profile.getJoinDate(),
                profile.getLastLogin(),
                stats
        );
    }

    @Override
    public String uploadAvatarByEmail(String email, MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Empty file");
        String contentType = file.getContentType();
        if (contentType == null || !(contentType.contains("jpeg") || contentType.contains("png") || contentType.contains("jpg"))) {
            throw new IllegalArgumentException("Invalid file type");
        }
        if (file.getSize() > 2 * 1024 * 1024) throw new IllegalArgumentException("File too large");

        UserEntity user = findUserByEmail(email);
        String url = avatarStorageService.store(user.getId().toString(), file);
        UserProfileEntity profile = profileRepository.findByUserId(user.getId()).orElseGet(() -> createDefaultProfileForUser(user));
        profile.setAvatarUrl(url);
        profileRepository.save(profile);
        return url;
    }

    @Override
    public UserStatsResource getStatsByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        return computeStats(user.getId().toString());
    }

    @Override
    public AchievementsResponse getAchievementsByEmail(String email) {
        UserEntity user = findUserByEmail(email);
        List<AchievementEntity> list = achievementRepository.findByUserId(user.getId().toString());
        var resources = list.stream().map(a -> new AchievementResource(
                a.getId(), a.getTitle(), a.getDescription(), a.getIcon(), a.getEarnedDate(), a.getStatus() == null ? "locked" : a.getStatus().name().toLowerCase()
        )).collect(Collectors.toList());
        return new AchievementsResponse(resources);
    }
}
