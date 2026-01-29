package org.bitmaxsystems.oop2library.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.List;

public class HibernateInit {
    private static final Logger logger = LogManager.getLogger(HibernateInit.class);
    public static void initializeIfEmpty()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            List<Credentials> credentials = session.createQuery("FROM Credentials",Credentials.class).list();

            if (credentials.isEmpty())
            {
                Transaction tx = session.beginTransaction();
                session.persist(new Credentials("admin", BCrypt.hashpw("admin",BCrypt.gensalt())));
                tx.commit();
                logger.info("Added admin user");
            }
            else
            {
                logger.info("Admin user already exist.");
            }
        }
    }

}
