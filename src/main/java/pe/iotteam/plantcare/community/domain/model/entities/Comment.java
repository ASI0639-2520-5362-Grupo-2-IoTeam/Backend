package pe.iotteam.plantcare.community.domain.model.entities;

import jakarta.persistence.*;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private CommunityMember author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    protected Comment() {}

    public Comment(CommunityMember author, Post post, String content) {
        this.author = author;
        this.post = post;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Post getPost() { return post; }
    public CommunityMember getAuthor() { return author; }

    // Setters controlados
    public void setPost(Post post) {
        this.post = post;
    }
}
