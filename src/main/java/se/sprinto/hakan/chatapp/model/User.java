package se.sprinto.hakan.chatapp.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;






@Entity
@Table(name= "users")
public class User {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Message> messages = new ArrayList<>();

    protected User() {

    }



    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public List<Message> getMessages() {
        return messages;
    }
    public void setId(Long id) {
        this.id = id;
    }


}

