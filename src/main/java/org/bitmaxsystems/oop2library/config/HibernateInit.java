package org.bitmaxsystems.oop2library.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.util.Date;
import java.util.List;

public class HibernateInit {
    private static final Logger logger = LogManager.getLogger(HibernateInit.class);
    public static void initializeIfEmpty()
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            List<Credentials> credentialsList = session.createQuery("FROM Credentials",Credentials.class).list();

            if (credentialsList.isEmpty())
            {
                User user = new User.Builder("Admin","Admin",21,"+359888000001",UserRole.ADMINISTRATOR)
                        .setDateOfApproval(new Date())
                        .build();
                Credentials credentials = new Credentials("admin", BCrypt.hashpw("admin",BCrypt.gensalt()),user);
                Transaction tx = session.beginTransaction();
                session.persist(user);
                session.persist(credentials);
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
