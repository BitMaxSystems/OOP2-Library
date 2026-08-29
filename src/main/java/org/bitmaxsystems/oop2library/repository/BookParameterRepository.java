package org.bitmaxsystems.oop2library.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BookParameterRepository {

    private static final Logger logger = LogManager.getLogger(BookParameterRepository.class);

    private static  BookParameterRepository repository = null;
    private BookParameterRepository()
    {}

    public static BookParameterRepository getInstance()
    {
        if (repository == null)
        {repository = new BookParameterRepository();}

        return repository;
    }

    public <T> List<T> findAllParameterRecords(Class<T> tClass)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("from " + tClass.getName(), tClass);
            List<T> result = query.list();

            logger.info("Loaded {} records from {}",  result.size(), tClass.getSimpleName());

            return result;
        } catch (Exception e) {
            logger.error("Failed to load {}", tClass.getSimpleName(), e);
            throw e;
        }
    }

    public int checkIfParameterExists(String parameterTable, String parameterName)
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM " + parameterTable + " WHERE name = :name", Long.class);
            query.setParameter("name", parameterName);
            return Math.toIntExact(query.getResultCount());
        }
    }

    public long checkCountOfBookWithParameter(IBookParameter parameter) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(b) FROM Book b WHERE b."+parameter.getClass().getSimpleName().toLowerCase()+" = :parameter",
                    Long.class
            );

            query.setParameter("parameter", parameter);

            long count = query.getSingleResult();

            logger.info("Found {}books with {} : {}", count, parameter.getClass().getSimpleName(), parameter.getName());

            return count;
        } catch (Exception e) {
            logger.error(e);
            throw e;
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
        logger.info("Parameter saved");
    }

    public void updateParameter(IBookParameter parameter)
    {
        Transaction transaction;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(parameter);
            transaction.commit();
        }

        logger.info("Parameter updated");


    }

    public void deleteParameter(IBookParameter parameter)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction;
            transaction = session.beginTransaction();
            session.remove(parameter);
            transaction.commit();
        }

        logger.info("Parameter deleted");

    }


}
