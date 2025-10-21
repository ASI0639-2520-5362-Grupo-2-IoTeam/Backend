package pe.iotteam.plantcare.community.domain.model.aggregates;

import jakarta.persistence.*;
import pe.iotteam.plantcare.auth.domain.model.entities.Role;
import pe.iotteam.plantcare.community.domain.model.entities.Post;
import pe.iotteam.plantcare.community.domain.model.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "community_members")
public class CommunityMember {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Embedded
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime joinedAt;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    protected CommunityMember() {}

    public CommunityMember(UserId userId, Role role) {
        this.userId = userId;
        this.role = role;
        this.joinedAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public UserId getUserId() { return userId; }
    public Role getRole() { return role; }
    public LocalDateTime getJoinedAt() { return joinedAt; }
    public List<Post> getPosts() { return posts; }

    // Behavior
    public void promoteToAdmin() {
        this.role = Role.ADMIN;
    }

    public void demoteToMember() {
        this.role = Role.USER;
    }

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
