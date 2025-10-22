package pe.iotteam.plantcare.community.domain.model.aggregates;

import jakarta.persistence.*;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.community.domain.model.entities.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "community_members")
public class CommunityMember {

    @Id
    @Column(nullable = false, updatable = false, unique = true)
    private UUID id; // ← mismo UUID del usuario

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    protected CommunityMember() {}

    public CommunityMember(UUID userId, Role role) {
        this.id = userId; // el mismo UUID del usuario
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public Role getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public List<Post> getPosts() { return posts; }

    // Behavior
    public void promoteToAdmin() { this.role = Role.ADMIN; }
    public void demoteToMember() { this.role = Role.USER; }

    public void addPost(Post post) {
        posts.add(post);
        post.setAuthor(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setAuthor(null);
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}