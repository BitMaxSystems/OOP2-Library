package org.bitmaxsystems.oop2library.models.form;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.form.enums.FormStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Table(name = "user_form")
public class UserForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDate dateOfCreation;
    @Enumerated
    private FormStatus status;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    protected UserForm() {
    }

    public UserForm(User user)
    {
        this.dateOfCreation = LocalDate.now();
        this.status = FormStatus.PENDING;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public FormStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public boolean approveForm()
    {
        if (status == FormStatus.PENDING)
        {
            this.status = FormStatus.APPROVED;
            user.approve();
            return true;
        }
        else
        {
            return false;
        }
    }
}
