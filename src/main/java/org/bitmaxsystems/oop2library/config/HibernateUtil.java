package org.bitmaxsystems.oop2library.config;

import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.books.Inventory;

import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration()
                    .configure("hibernate.cfg.xml")

                    .addAnnotatedClass(Author.class)
                    .addAnnotatedClass(Genre.class)
                    .addAnnotatedClass(Publisher.class)
                    .addAnnotatedClass(Book.class)
                    .addAnnotatedClass(Inventory.class)
                    .addAnnotatedClass(Credentials.class)
                    .addAnnotatedClass(User.class)
                    .addAnnotatedClass(UserForm.class);

            return configuration.buildSessionFactory();
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
