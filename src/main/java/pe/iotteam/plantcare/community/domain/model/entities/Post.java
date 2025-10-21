package pe.iotteam.plantcare.community.domain.model.entities;

import jakarta.persistence.*;
import pe.iotteam.plantcare.community.domain.model.aggregates.CommunityMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private CommunityMember author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Post() {}

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CommunityMember getAuthor() { return author; }
    public List<Comment> getComments() { return comments; }

    // Setters (limitados)
    public void setAuthor(CommunityMember author) {
        this.author = author;
    }

    // Behavior
    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }
}

