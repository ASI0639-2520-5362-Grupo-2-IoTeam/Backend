package pe.iotteam.plantcare.community.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    List<Post> findAllByOrderByCreatedAtDesc();

    List<Post> findByAuthor(CommunityMember author);
}
