package pe.iotteam.plantcare.user.interfaces.rest;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.iotteam.plantcare.user.application.internal.services.UserProfileService;
import pe.iotteam.plantcare.user.interfaces.rest.resources.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    // GET /api/v1/users/profile
    @GetMapping("/profile")
    public ResponseEntity<UserProfileResource> getProfile(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getProfileByEmail(email));
    }

    // PATCH /api/v1/users/profile
    @PatchMapping("/profile")
    public ResponseEntity<UserProfileResource> updateProfile(Authentication authentication,
                                                             @Valid @RequestBody UpdateProfileRequest request) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.updateProfileByEmail(email, request));
    }

    // POST /api/v1/users/profile/avatar
    @PostMapping(value = "/profile/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadAvatar(Authentication authentication,
                                          @RequestPart("file") MultipartFile file) throws Exception {
        String email = authentication.getName();
        String url = service.uploadAvatarByEmail(email, file);
        return ResponseEntity.ok().body(java.util.Collections.singletonMap("avatarUrl", url));
    }

    // GET /api/v1/users/stats
    @GetMapping("/stats")
    public ResponseEntity<UserStatsResource> getStats(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getStatsByEmail(email));
    }

    // GET /api/v1/users/achievements
    @GetMapping("/achievements")
    public ResponseEntity<AchievementsResponse> getAchievements(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(service.getAchievementsByEmail(email));
    }
}
