package org.bitmaxsystems.oop2library.models.users;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.form.UserForm;
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
    private int age;
    private String phone;
    private int loyaltyPoints = 50;
    @Enumerated
    private UserRole role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserForm form;



    public static class Builder
    {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private UserRole role;


        public Builder(String firstName, String lastName, String phone, UserRole role)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.phone = phone;
            this.role = role;
        }

        public Builder setAge (int age)
        {
            this.age = age;
            return this;
        }

        public User build()
        {
            return new User(this.firstName,this.lastName,this.age,this.phone,this.role);
        }
    }
        public User() {
        }

        private User (String firstName, String lastName, int age, String phone, UserRole role)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.phone = phone;
            this.role = role;
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

    public int getAge() {
        return age;
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

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void updateLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints += loyaltyPoints;
    }
}
