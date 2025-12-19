package se.sprinto.hakan.chatapp.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    protected Message() {

    }

    public Message(User user, String text, LocalDateTime timestamp) {
        this.user = user;
        this.text = text;
        this.timestamp = timestamp;
    }
    public Long getId() {
        return id;
    }
    public String getText() {
        return text;
    }
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public User getUser() {
        return user;
    }
}
