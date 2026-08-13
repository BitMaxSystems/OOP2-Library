package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    private Book book;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookStatus status;

    protected Inventory() {
    }

    public Inventory(Book book) {
        this.book = book;
        this.status = BookStatus.AVAILABLE;
    }

    public int getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }
}