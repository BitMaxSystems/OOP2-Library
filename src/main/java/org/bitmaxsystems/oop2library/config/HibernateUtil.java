package org.bitmaxsystems.oop2library.config;
import java.io.InputStream;
import java.util.Properties;

import org.bitmaxsystems.oop2library.model.books.Author;
import org.bitmaxsystems.oop2library.model.books.Book;
import org.bitmaxsystems.oop2library.model.books.Genre;
import org.bitmaxsystems.oop2library.model.books.Publisher;
import org.bitmaxsystems.oop2library.model.books.Inventory;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Properties dbProps = new Properties();
            try (InputStream is = HibernateUtil.class
                    .getClassLoader()
                    .getResourceAsStream("db.properties")) {

                if (is == null) {
                    throw new RuntimeException("db.properties not found in resources");
                }
                dbProps.load(is);
            }

            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addProperties(dbProps)

                    .addAnnotatedClass(Author.class)
                    .addAnnotatedClass(Genre.class)
                    .addAnnotatedClass(Publisher.class)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(Inventory.class);

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
