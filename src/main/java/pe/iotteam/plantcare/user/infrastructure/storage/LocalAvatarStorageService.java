package pe.iotteam.plantcare.user.infrastructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class LocalAvatarStorageService implements AvatarStorageService {

    private final Path baseDir;

    public LocalAvatarStorageService(@Value("${app.storage.avatars:uploads/avatars}") String baseDir) throws IOException {
        this.baseDir = Path.of(baseDir);
        Files.createDirectories(this.baseDir);
    }

    @Override
    public String store(String userId, MultipartFile file) throws IOException {
        String original = file.getOriginalFilename();
        String ext = "";
        if (original != null && original.contains(".")) {
            ext = original.substring(original.lastIndexOf('.'));
        }
        String filename = userId + "-" + UUID.randomUUID() + ext;
        Path target = baseDir.resolve(filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        // Return a simple file:// URL so caller can use it; in production replace with S3 URL
        return target.toAbsolutePath().toUri().toString();
    }
}
