package pe.iotteam.plantcare.user.infrastructure.storage;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class LocalAvatarStorageServiceTest {

    @Test
    public void store_shouldSaveFile() throws IOException {
        LocalAvatarStorageService svc = new LocalAvatarStorageService("target/test-avatars");
        MockMultipartFile file = new MockMultipartFile("file", "avatar.png", "image/png", "abc".getBytes());
        String url = svc.store("user-1", file);
        assertNotNull(url);
        assertTrue(url.contains("user-1"));
    }
}
