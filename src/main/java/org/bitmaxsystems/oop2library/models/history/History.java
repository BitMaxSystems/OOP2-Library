package org.bitmaxsystems.oop2library.models.history;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.users.User;

import java.time.LocalDate;
import java.util.Objects;

@Table(name = "history")
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(nullable = false, name = "date_of_lending")
    private LocalDate dateOfLending;

    @Column(nullable = false, name = "expected_return_date")
    private LocalDate expectedReturnDate;

    @Column(name = "actual_return_date")
    private LocalDate actualReturnDate;

    protected History ()
    {}

    private History (User user, Inventory inventory, LocalDate dateOfLending, LocalDate expectedReturnDate)
    {
        this.user = user;
        this.inventory = inventory;
        this.dateOfLending = dateOfLending;
        this.expectedReturnDate = expectedReturnDate;
    }

    public static class Builder
    {
        private User user;
        private Inventory inventory;
        private LocalDate dateOfLending;
        private LocalDate expectedReturnDate;

        public Builder(User user, Inventory inventory)
        {
            this.user = user;
            this.inventory = inventory;
        }

        public Builder lendOutside()
        {
            this.dateOfLending = LocalDate.now();
            this.expectedReturnDate = LocalDate.now().plusMonths(1);
            return this;
        }


        public Builder lendInside()
        {
            this.dateOfLending = LocalDate.now();
            this.expectedReturnDate = LocalDate.now();
            return this;
        }

        public History build()
        {
            return new History(this.user,this.inventory,this.dateOfLending,this.expectedReturnDate);
        }
    }

    @Transient
    public LendStatusEnum getLendStatus()
    {
        if (Objects.nonNull(actualReturnDate))
        {
            if (actualReturnDate.isBefore(expectedReturnDate) || actualReturnDate.isEqual(expectedReturnDate))
            {
                return LendStatusEnum.RETURNED;
            }
            else
            {
                return LendStatusEnum.RETURNED_OVERDUE;
            }
        }
        else if (expectedReturnDate.isBefore(LocalDate.now()))
        {
            return LendStatusEnum.OVERDUE;
        }

        return LendStatusEnum.IN_TIME;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public LocalDate getDateOfLending() {
        return dateOfLending;
    }

    public LocalDate getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public LocalDate getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
}
