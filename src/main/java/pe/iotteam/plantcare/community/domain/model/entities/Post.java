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
    @JoinColumn(name = "author_id", nullable = false)
    private CommunityMember author;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column
    private String species; // Nueva propiedad

    @Column
    private String tag; // Nueva propiedad

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean highlighted = false;

    @Column(name = "highlighted_at")
    private LocalDateTime highlightedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    protected Post() {}

    public Post(String title, String content, String species, String tag, CommunityMember author) {
        this.title = title;
        this.content = content;
        this.species = species;
        this.tag = tag;
        this.author = author;
        this.createdAt = LocalDateTime.now();
    }

    // Getters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getSpecies() { return species; }
    public String getTag() { return tag; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public CommunityMember getAuthor() { return author; }
    public boolean isHighlighted() { return highlighted; }
    public LocalDateTime getHighlightedAt() { return highlightedAt; }
    public List<Comment> getComments() { return comments; }

    // Setters controlados
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
    // Comportamiento de dominio
    public void markAsHighlighted() {
        this.highlighted = true;
        this.highlightedAt = LocalDateTime.now();
    }

    public void removeHighlight() {
        this.highlighted = false;
        this.highlightedAt = null;
    }
}