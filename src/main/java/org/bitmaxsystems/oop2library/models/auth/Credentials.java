package org.bitmaxsystems.oop2library.models.auth;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.users.User;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.UUID;

@Entity
@Table(name = "credentials")
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name="id")
    private UUID id;
    private String username;
    private String password;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;


    public Credentials(String username, String password, User user)
    {
        this.username = username;
        this.password = password;
        this.user = user;
    }

    protected Credentials() {

    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public User getUser() {
        return user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(String username, String password) {
        return this.username.equals(username) && BCrypt.checkpw(password,this.password);
    }
}
