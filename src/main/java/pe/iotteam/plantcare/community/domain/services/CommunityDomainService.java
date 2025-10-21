package pe.iotteam.plantcare.community.domain.services;

import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;
import pe.iotteam.plantcare.community.domain.exceptions.InvalidCommunityActionException;

public class CommunityDomainService {

    public Post createPost(CommunityMember member, String title, String content) {
        if (member == null) throw new InvalidCommunityActionException("Miembro no válido.");
        Post post = new Post(title, content);
        member.addPost(post);
        return post;
    }

    public Comment addComment(Post post, String content) {
        if (post == null) throw new InvalidCommunityActionException("Publicación no válida.");
        Comment comment = new Comment(content);
        post.addComment(comment);
        return comment;
    }

    public void deletePost(CommunityMember member, Post post) {
        if (!member.isAdmin())
            throw new InvalidCommunityActionException("Solo los administradores pueden eliminar publicaciones.");
        member.removePost(post);
    }

    public void deleteComment(CommunityMember member, Comment comment) {
        if (!member.isAdmin())
            throw new InvalidCommunityActionException("Solo los administradores pueden eliminar comentarios.");
        comment.setPost(null);
    }
}
