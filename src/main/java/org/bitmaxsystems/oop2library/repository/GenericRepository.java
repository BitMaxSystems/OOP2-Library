package org.bitmaxsystems.oop2library.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class GenericRepository<T> {

    private final Class<T> type;
    private static final Logger logger = LogManager.getLogger(GenericRepository.class);

    public GenericRepository(Class<T> type) {
        this.type = type;
    }

    public void save(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error(e);
        }
    }


    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error(e);
        }
    }


    public T findById(Object id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.find(type, id);
        }
        catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("from " + type.getName(), type);
            return query.list();
        }
        catch (Exception e) {
            logger.error(e);
        }
        return null;
    }

    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(entity);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            logger.error(e);
        }
    }
}