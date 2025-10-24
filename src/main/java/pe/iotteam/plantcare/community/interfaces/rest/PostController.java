package pe.iotteam.plantcare.community.interfaces.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.iotteam.plantcare.community.application.internal.commandservices.CommunityContentCommandService;
import pe.iotteam.plantcare.community.application.internal.queryservices.CommunityFeedQueryService;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.interfaces.rest.resources.PostResource;
import pe.iotteam.plantcare.community.interfaces.rest.transform.PostResourceAssembler;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/community/posts")
public class PostController {

    private final CommunityContentCommandService contentCommandService;
    private final CommunityFeedQueryService feedQueryService;

    public PostController(
            CommunityContentCommandService contentCommandService,
            CommunityFeedQueryService feedQueryService) {
        this.contentCommandService = contentCommandService;
        this.feedQueryService = feedQueryService;
    }

    @PostMapping
    public ResponseEntity<PostResource> createPost(
            @RequestParam UUID userId,
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String species,
            @RequestParam String tag
    ) {
        Post post = contentCommandService.createPost(userId, title, content, species, tag);
        return ResponseEntity.ok(PostResourceAssembler.toResource(post));
    }

    @GetMapping
    public ResponseEntity<List<PostResource>> getAllPosts() {
        var posts = feedQueryService.getAllPosts()
                .stream()
                .map(PostResourceAssembler::toResource)
                .toList();
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deleteOwnPost(@RequestParam UUID userId, @PathVariable Long postId) {
        contentCommandService.deleteOwnPost(userId, postId);
        return ResponseEntity.noContent().build();
    }
}
