package pe.iotteam.plantcare.community.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.community.application.internal.commandservices.CommunityContentCommandService;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;

import java.util.UUID;

@RestController
@RequestMapping("/api/community/comments")
public class CommentController {

    private final CommunityContentCommandService contentCommandService;

    public CommentController(CommunityContentCommandService contentCommandService) {
        this.contentCommandService = contentCommandService;
    }

    @PostMapping
    public ResponseEntity<Comment> addComment(
            @RequestParam UUID userId,
            @RequestParam Long postId,
            @RequestParam String text
    ) {
        Comment comment = contentCommandService.addComment(userId, postId, text);
        return ResponseEntity.ok(comment);
    }
}
