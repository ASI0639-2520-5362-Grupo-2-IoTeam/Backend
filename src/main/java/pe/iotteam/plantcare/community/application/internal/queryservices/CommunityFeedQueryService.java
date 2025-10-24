package pe.iotteam.plantcare.community.application.internal.queryservices;

import org.springframework.stereotype.Service;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.PostRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CommunityFeedQueryService {

    private final PostRepository postRepository;

    public CommunityFeedQueryService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    /**
     * Devuelve todas las publicaciones ordenadas por fecha descendente.
     */
    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Devuelve una publicación específica con sus comentarios.
     */
    public Post getPostById(UUID postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
    }
}

