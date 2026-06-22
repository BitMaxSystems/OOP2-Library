package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.loans.enums.LoanStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class LoanRepository extends GenericRepository<Loan> {

    public LoanRepository() {
        super(Loan.class);
    }

    public List<Loan> findActiveByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                    "FROM Loan WHERE user = :user AND status = :status", Loan.class);
            query.setParameter("user", user);
            query.setParameter("status", LoanStatus.ACTIVE);
            return query.list();
        }
    }

    public List<Loan> findAllByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                    "FROM Loan WHERE user = :user", Loan.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    public List<Loan> findOverdue() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                    "FROM Loan WHERE status = :status AND dueDate < CURRENT_TIMESTAMP", Loan.class);
            query.setParameter("status", LoanStatus.ACTIVE);
            return query.list();
        }
    }

    public Loan findActiveByInventory(Inventory inventory) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Loan> query = session.createQuery(
                    "FROM Loan WHERE inventory = :inventory AND status = :status", Loan.class);
            query.setParameter("inventory", inventory);
            query.setParameter("status", LoanStatus.ACTIVE);
            return query.uniqueResultOptional().orElse(null);
        }
    }

    public int countActiveByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM Loan WHERE user = :user AND status = :status", Long.class);
            query.setParameter("user", user);
            query.setParameter("status", LoanStatus.ACTIVE);
            return Math.toIntExact(query.getSingleResult());
        }
    }
}