package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;
import lombok.*;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;

@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @ToString.Exclude
    private Book book;

    @Enumerated(EnumType.STRING)
    private BookStatus status;

    public Inventory(Book book, BookStatus status) {
        this.book = book;
        this.status = status;
    }
}