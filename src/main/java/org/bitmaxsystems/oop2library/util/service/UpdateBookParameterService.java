package org.bitmaxsystems.oop2library.util.service;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UpdateBookParameterService {

    public void update(BookParameter parameter)
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(parameter);
            transaction.commit();
        }

    }
}

