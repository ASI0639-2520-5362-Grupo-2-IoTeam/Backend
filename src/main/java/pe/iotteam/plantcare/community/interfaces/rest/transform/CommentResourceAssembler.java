package pe.iotteam.plantcare.community.interfaces.rest.transform;

import pe.iotteam.plantcare.community.domain.model.entities.Comment;
import pe.iotteam.plantcare.community.interfaces.rest.resources.CommentResource;

public class CommentResourceAssembler {

    public static CommentResource toResource(Comment comment) {
        return new CommentResource(
                comment.getId(),
                comment.getPost().getId().toString(),
                comment.getContent(),
                comment.getCreatedAt()
        );
    }
}