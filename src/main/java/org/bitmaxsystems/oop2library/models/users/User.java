package org.bitmaxsystems.oop2library.models.users;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;

import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;
    private String firstName;
    private String lastName;
    private int years;
    private String phone;
    private int loyaltyPoints;
    @Enumerated
    private UserRole role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;

    public User(){}

    public User(String firstName, String lastName, int years, String phone, UserRole role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.years = years;
        this.phone = phone;
        this.role = role;
        this.loyaltyPoints = 50;
    }

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getYears() {
        return years;
    }

    public String getPhone() {
        return phone;
    }

    public UserRole getRole() {
        return role;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void updateLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints += loyaltyPoints;
    }
}
