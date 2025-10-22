package pe.iotteam.plantcare.community.application.internal.commandservices;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;
import pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories.*;

import java.util.UUID;

@Service
public class CommunityContentCommandService {

    private final CommunityMemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommunityContentCommandService(
            CommunityMemberRepository memberRepository,
            PostRepository postRepository,
            CommentRepository commentRepository
    ) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public Post createPost(UserId userId, String title, String content, String species, String tag) {
        CommunityMember author = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Miembro no encontrado"));

        Post post = new Post(title, content, species, tag, author);
        return postRepository.save(post);
    }

    @Transactional
    public Comment addComment(UserId userId, UUID postId, String text) {
        CommunityMember author = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Miembro no encontrado"));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Publicación no encontrada"));

        Comment comment = new Comment(author, post, text);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteOwnPost(UserId userId, UUID postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post no encontrado"));
        if (!post.getAuthor().getUserId().equals(userId))
            throw new RuntimeException("No autorizado para eliminar este post");
        postRepository.delete(post);
    }
}

