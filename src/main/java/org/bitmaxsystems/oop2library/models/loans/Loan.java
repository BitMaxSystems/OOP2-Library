package org.bitmaxsystems.oop2library.models.loans;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.loans.enums.LoanStatus;
import org.bitmaxsystems.oop2library.models.users.User;

import java.util.Date;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date borrowedDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dueDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date returnedDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    public Loan() {}

    public Loan(Inventory inventory, User user, Date dueDate) {
        if (inventory == null) throw new NullPointerException("Inventory cannot be null");
        if (user == null) throw new NullPointerException("User cannot be null");
        if (dueDate == null) throw new NullPointerException("Due date cannot be null");

        this.inventory = inventory;
        this.user = user;
        this.borrowedDate = new Date();
        this.dueDate = dueDate;
        this.status = LoanStatus.ACTIVE;
    }

    public boolean isOverdue() {
        return status == LoanStatus.ACTIVE && new Date().after(dueDate);
    }

    public void markReturned() {
        this.returnedDate = new Date();
        this.status = LoanStatus.RETURNED;
    }

    public void markOverdue() {
        this.status = LoanStatus.OVERDUE;
    }

    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        long diff = new Date().getTime() - dueDate.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    public Long getId() { return id; }
    public Inventory getInventory() { return inventory; }
    public User getUser() { return user; }
    public Date getBorrowedDate() { return borrowedDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnedDate() { return returnedDate; }
    public LoanStatus getStatus() { return status; }
}