package org.bitmaxsystems.oop2library.models.history;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.users.User;

import java.time.LocalDate;

@Table(name = "history")
@Entity
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(nullable = false, name = "date_of_lending")
    private LocalDate dateOfLending;

    @Column(nullable = false, name = "expected_return_date")
    private LocalDate expectedReturnDate;

    @Column(nullable = true, name = "actual_return_date")
    private LocalDate actualReturnDate;


    protected History ()
    {}

    public History (User user, Inventory inventory)
    {
        this.user = user;
        this.inventory = inventory;
        this.dateOfLending = LocalDate.now();
        this.expectedReturnDate = LocalDate.now().plusMonths(1);
    }
}
