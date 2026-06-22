package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.reservations.Reservation;
import org.bitmaxsystems.oop2library.models.reservations.enums.ReservationStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class ReservationRepository extends GenericRepository<Reservation> {

    public ReservationRepository() {
        super(Reservation.class);
    }

    public List<Reservation> findPendingByBook(Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE book = :book AND status = :status ORDER BY reservationDate ASC",
                    Reservation.class);
            query.setParameter("book", book);
            query.setParameter("status", ReservationStatus.PENDING);
            return query.list();
        }
    }

    public List<Reservation> findAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE user = :user ORDER BY reservationDate DESC",
                    Reservation.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    public Reservation findPendingByUserAndBook(User user, Book book) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Reservation> query = session.createQuery(
                    "FROM Reservation WHERE user = :user AND book = :book AND status = :status",
                    Reservation.class);
            query.setParameter("user", user);
            query.setParameter("book", book);
            query.setParameter("status", ReservationStatus.PENDING);
            return query.uniqueResultOptional().orElse(null);
        }
    }
}