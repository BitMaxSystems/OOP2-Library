package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepository {

    private static UserRepository repository = null;
    private UserRepository() {}

    public static UserRepository getInstance()
    {
        if (repository == null)
        {
            repository = new UserRepository();
        }

        return repository;
    }

    public List<User> searchByRole(UserRole role)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Query<User> query = session.createQuery("FROM User WHERE role = :role", User.class);
            query.setParameter("role",role);
            return query.list();
        }
    }

    public int countExistingUsersByUsername (String username)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM Credentials where username =:username ", Long.class);
            query.setParameter("username",username);
            return Math.toIntExact(query.getSingleResult());
        }
    }

    public void createUserInDatabase(User user, Credentials credentials)
    {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            session.persist(credentials);
            transaction.commit();

        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            throw e;
        }
    }
}
