package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.hibernate.Session;

public class AuthorisationRepository {

    public Credentials getUserAuthorisation(String username)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            return session.createQuery("FROM Credentials WHERE username = :username", Credentials.class)
                    .setParameter("username", username)
                    .uniqueResultOptional()
                    .orElse(null);
        }
    }
}
