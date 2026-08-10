package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class BookParameterRepository {

    private static  BookParameterRepository repository = null;
    private BookParameterRepository()
    {}

    public static BookParameterRepository getInstance()
    {
        if (repository == null)
        {repository = new BookParameterRepository();}

        return repository;
    }

    public int checkIfParameterExists(String parameterTable, String parameterName)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM " + parameterTable + " WHERE name = :name", Long.class);
            query.setParameter("name", parameterName);
            return Math.toIntExact(query.getResultCount());
        }
    }

    public int checkIfBooksWithParametersExist(String parameterTable, IBookParameter parameter)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM "+ Book.class.getSimpleName() +" where "+parameterTable+"=:id"
                    ,Long.class);

            query.setParameter("id",parameter);
            return Math.toIntExact(query.getResultCount());
        }
    }

    public void saveParameter(IBookParameter parameter)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            transaction = session.beginTransaction();
            session.persist(parameter);
            transaction.commit();
        }
    }

    public void updateParameter(IBookParameter parameter)
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(parameter);
            transaction.commit();
        }

    }

    public void deleteParameter(IBookParameter parameter)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            transaction = session.beginTransaction();
            session.remove(parameter);
            transaction.commit();
        }
    }


}
