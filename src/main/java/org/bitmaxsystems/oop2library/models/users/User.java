package org.bitmaxsystems.oop2library.models.users;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
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
    private LocalDate dateOfApproval;
    @Enumerated
    private UserRole role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Credentials credentials;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserForm form;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<History> history;



    public static class Builder
    {
        private String firstName;
        private String lastName;
        private int age;
        private String phone;
        private UserRole role;
        private LocalDate dateOfApproval;


        public Builder(String firstName, String lastName, int age ,String phone, UserRole role)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.phone = phone;
            this.role = role;
        }

        public Builder setDateOfApproval (LocalDate dateOfApproval)
        {
            this.dateOfApproval = dateOfApproval;
            return this;
        }

        public User build()
        {
            return new User(this.firstName,this.lastName,this.age,this.phone,this.dateOfApproval,this.role);
        }
    }
        protected User() {
        }

        private User (String firstName, String lastName, int age, String phone, LocalDate dateOfApproval ,UserRole role)
        {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
            this.phone = phone;
            this.dateOfApproval = dateOfApproval;
            this.role = role;
        }

    public Credentials getCredentials() {
        return credentials;
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

    public LocalDate getDateOfApproval() {
        return dateOfApproval;
    }

    public UserRole getRole() {
        return role;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public UserForm getForm() {
        return form;
    }

    public void updateLoyaltyPoints(int loyaltyPoints) {

        if (loyaltyPoints > 100-this.loyaltyPoints)
        {
            loyaltyPoints = 100-this.loyaltyPoints;
        }
        else if (loyaltyPoints < -this.loyaltyPoints)
        {
            loyaltyPoints = -this.loyaltyPoints;
        }
        this.loyaltyPoints += loyaltyPoints;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public void approve()
    {
        this.role = UserRole.READER;
        this.dateOfApproval = LocalDate.now();
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " - " + this.loyaltyPoints+"/100";
    }
}
