package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;

@Entity
@Table(name = "books")

public class Book {

    @Id
    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    private Author author;

    @ManyToOne(optional = false)
    private Genre genre;

    @ManyToOne(optional = false)
    private Publisher publisher;

    protected Book () {}
    public Book(String isbn, String title, Author author, Genre genre, Publisher publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }


    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public Genre getGenre() {
        return genre;
    }

    public Publisher getPublisher() {
        return publisher;
    }
}