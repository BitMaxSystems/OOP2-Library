package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.config.BookStatusConverter;
import org.bitmaxsystems.oop2library.util.bookstatus.AvailableBookStatus;
import org.bitmaxsystems.oop2library.util.bookstatus.IBookStatus;

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
    private IBookStatus status;

    @Column(nullable = false)
    private boolean archived;

    protected Inventory() {
    }

    public Inventory(Book book) {
        this.book = book;
        this.status = new AvailableBookStatus();
        this.archived = false;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public IBookStatus getStatus() {
        return status;
    }

    public void setStatus(IBookStatus status) {
        this.status = status;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}