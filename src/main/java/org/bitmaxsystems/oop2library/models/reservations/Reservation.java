package org.bitmaxsystems.oop2library.models.reservations;

import jakarta.persistence.*;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.reservations.enums.ReservationStatus;
import org.bitmaxsystems.oop2library.models.users.User;

import java.util.Date;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    private Date reservationDate;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    public Reservation() {}

    public Reservation(Book book, User user) {
        if (book == null) throw new NullPointerException("Book cannot be null");
        if (user == null) throw new NullPointerException("User cannot be null");

        this.book = book;
        this.user = user;
        this.reservationDate = new Date();
        this.status = ReservationStatus.PENDING;
    }

    public void fulfil() {
        this.status = ReservationStatus.FULFILLED;
    }

    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }

    public Long getId() { return id; }
    public Book getBook() { return book; }
    public User getUser() { return user; }
    public Date getReservationDate() { return reservationDate; }
    public ReservationStatus getStatus() { return status; }
}