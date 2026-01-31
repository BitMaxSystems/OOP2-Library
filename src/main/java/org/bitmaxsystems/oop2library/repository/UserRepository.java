package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class UserRepository {

    public List<User> searchByRole(UserRole role)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Query<User> query = session.createQuery("FROM User WHERE role = :role", User.class);
            query.setParameter("role",role);
            return query.list();
        }
    }
}
