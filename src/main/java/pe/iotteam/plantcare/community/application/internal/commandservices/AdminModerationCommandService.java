package pe.iotteam.plantcare.community.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.*;

import java.util.UUID;

@Service
public class AdminModerationCommandService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public AdminModerationCommandService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public void deletePostByAdmin(UUID postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public void deleteCommentByAdmin(UUID commentId) {
        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void highlightPost(UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        post.markAsHighlighted();
        postRepository.save(post);
    }
}

