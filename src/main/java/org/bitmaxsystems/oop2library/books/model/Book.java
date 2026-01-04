package org.bitmaxsystems.oop2library.books.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String isbn;

    @Column(nullable = false)
    private String title;

    @ManyToOne(optional = false)
    @ToString.Exclude
    private Author author;

    @ManyToOne(optional = false)
    @ToString.Exclude
    private Genre genre;

    @ManyToOne(optional = false)
    @ToString.Exclude
    private Publisher publisher;

    public Book(String isbn, String title, Author author, Genre genre, Publisher publisher) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publisher = publisher;
    }
}