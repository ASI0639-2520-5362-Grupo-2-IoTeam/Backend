package pe.iotteam.plantcare.user.infrastructure.storage;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarStorageService {
    String store(String userId, MultipartFile file) throws Exception;
}
