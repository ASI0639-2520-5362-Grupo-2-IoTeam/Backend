package pe.iotteam.plantcare.community.domain.services;

import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.domain.model.entities.Comment;
import pe.iotteam.plantcare.community.domain.exceptions.InvalidCommunityActionException;

//Provisional
public class CommunityDomainService {

    /**
     * Crea una nueva publicación asociada al miembro de la comunidad.
     * Solo se requiere que el miembro sea válido.
     */
    public Post createPost(CommunityMember member, String title, String content, String species, String tag) {
        if (member == null)
            throw new InvalidCommunityActionException("Miembro no válido.");
        if (title == null || title.isBlank())
            throw new InvalidCommunityActionException("El título no puede estar vacío.");
        if (content == null || content.isBlank())
            throw new InvalidCommunityActionException("El contenido no puede estar vacío.");

        Post post = new Post(title, content, species, tag, member);
        member.addPost(post);
        return post;
    }

    /**
     * Agrega un comentario a una publicación existente.
     * Solo un miembro válido puede hacerlo.
     */
    public Comment addComment(CommunityMember member, Post post, String content) {
        if (member == null)
            throw new InvalidCommunityActionException("Miembro no válido.");
        if (post == null)
            throw new InvalidCommunityActionException("Publicación no válida.");
        if (content == null || content.isBlank())
            throw new InvalidCommunityActionException("El comentario no puede estar vacío.");

        Comment comment = new Comment(member, post, content);
        post.addComment(comment);
        return comment;
    }

    /**
     * Elimina un post. Solo los administradores pueden eliminar cualquier post.
     * Los usuarios normales solo pueden eliminar los suyos.
     */
    public void deletePost(CommunityMember member, Post post) {
        if (member == null || post == null)
            throw new InvalidCommunityActionException("Miembro o publicación no válidos.");

        boolean isOwner = post.getAuthor().getId().equals(member.getId());

        if (!member.isAdmin() && !isOwner)
            throw new InvalidCommunityActionException("No autorizado para eliminar esta publicación.");

        member.removePost(post);
    }
    /**
     * Elimina un comentario. Los administradores pueden eliminar cualquier comentario,
     * los usuarios normales solo los suyos.
     */
    public void deleteComment(CommunityMember member, Comment comment) {
        if (member == null || comment == null)
            throw new InvalidCommunityActionException("Miembro o comentario no válidos.");

        boolean isOwner = comment.getAuthor().getId().equals(member.getId());

        if (!member.isAdmin() && !isOwner)
            throw new InvalidCommunityActionException("No autorizado para eliminar este comentario.");

        comment.setPost(null);
    }
}
