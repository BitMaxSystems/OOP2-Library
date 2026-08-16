package org.bitmaxsystems.oop2library.models.inventory;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.config.BookStatusConverter;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.IInventoryState;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Book book;

    @Convert(converter = BookStatusConverter.class)
    @Column(nullable = false)
    private IInventoryState status;

    @Column(nullable = false)
    private boolean archived;

    protected Inventory() {
    }

    public Inventory(Book book) {
        this.book = book;
        this.status = new AvailableInventoryState(this);
        this.archived = false;
    }

    @PostLoad
    private void postLoad()
    {
        this.status.setInventory(this);
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public IInventoryState getStatus() {
        return status;
    }

    public void setStatus(IInventoryState status) {
        this.status = status;
    }

    public void lendInside()
    {
        status.lendInside();
    }

    public void lendOutside()
    {
        status.lendOutside();
    }

    public void returnBook()
    {
        status.returnBook();
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}