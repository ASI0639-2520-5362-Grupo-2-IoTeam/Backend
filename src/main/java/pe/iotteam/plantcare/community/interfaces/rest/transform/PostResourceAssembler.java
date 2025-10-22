package pe.iotteam.plantcare.community.interfaces.rest.transform;

import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.interfaces.rest.resources.PostResource;

public class PostResourceAssembler {

    public static PostResource toResource(Post post) {
        return new PostResource(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor().getId().toString(),
                post.getCreatedAt(),
                post.isHighlighted()
        );
    }
}