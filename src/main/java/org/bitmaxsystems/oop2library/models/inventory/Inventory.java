package org.bitmaxsystems.oop2library.models.inventory;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.config.BookStatusConverter;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.IInventoryState;

import java.util.ArrayList;
import java.util.List;

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
    private IInventoryState state;

    @Column(nullable = false)
    private boolean archived;

    @OneToMany(mappedBy = "inventory", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<History> history = new ArrayList<>();

    protected Inventory() {
    }

    public Inventory(Book book) {
        this.book = book;
        this.state = new AvailableInventoryState(this);
        this.archived = false;
    }

    @PostLoad
    private void postLoad()
    {
        this.state.setInventory(this);
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public IInventoryState getState() {
        return state;
    }

    public void setState(IInventoryState state) {
        this.state = state;
    }

    public void lendInside()
    {
        state.lendInside();
    }

    public void lendOutside()
    {
        state.lendOutside();
    }

    public void returnBook()
    {
        state.returnBook();
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    @Override
    public String toString() {
        return id + " - "+book.toString();
    }
}