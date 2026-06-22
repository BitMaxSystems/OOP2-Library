package org.bitmaxsystems.oop2library.models.dto;

import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.users.User;

import java.util.Date;

public class LoanDataDTO {

    private final User user;
    private final Inventory inventory;
    private final Date dueDate;
    private Loan loan;

    public static class Builder {
        private final User user;
        private final Inventory inventory;
        private final Date dueDate;

        public Builder(User user, Inventory inventory, Date dueDate) {
            if (user == null) throw new NullPointerException("User cannot be null");
            if (inventory == null) throw new NullPointerException("Inventory cannot be null");
            if (dueDate == null) throw new NullPointerException("Due date cannot be null");

            this.user = user;
            this.inventory = inventory;
            this.dueDate = dueDate;
        }

        public LoanDataDTO build() {
            return new LoanDataDTO(this);
        }
    }

    private LoanDataDTO(Builder builder) {
        this.user = builder.user;
        this.inventory = builder.inventory;
        this.dueDate = builder.dueDate;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public User getUser() { return user; }
    public Inventory getInventory() { return inventory; }
    public Date getDueDate() { return dueDate; }
    public Loan getLoan() { return loan; }
}