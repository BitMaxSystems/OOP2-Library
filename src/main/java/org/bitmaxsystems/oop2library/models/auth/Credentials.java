package org.bitmaxsystems.oop2library.models.auth;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.UUID;

@Entity
@Table(name = "credentials")
public class Credentials {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String password;


    public Credentials(String username, String password)
    {
        this.username = username;
        this.password = password;
    }

    public Credentials() {

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

    public boolean equals(String username, String password) {
        return this.username.equals(username) && BCrypt.checkpw(password,this.password);
    }
}
