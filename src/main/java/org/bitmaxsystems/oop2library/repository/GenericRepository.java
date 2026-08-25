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

            logger.info("Saved {}", type.getSimpleName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Failed to save {}", type.getSimpleName(), e);
            throw e;
        }
    }

    public void saveAll(List<T> entities) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            for (T entity : entities) {
                session.persist(entity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            logger.error(e);
        }
    }


    public void update(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.merge(entity);

            transaction.commit();

            logger.info("Updated {}", type.getSimpleName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Failed to update {}", type.getSimpleName(), e);
            throw e;
        }
    }


    public T findById(Object id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            T result = session.find(type, id);

            if (result != null) {
                logger.info("Found {} with id {}", type.getSimpleName(), id);
            } else {
                logger.info("{} with id {} was not found", type.getSimpleName(), id);
            }

            return result;
        } catch (Exception e) {
            logger.error("Failed to find {} with id {}", type.getSimpleName(), id, e);
            throw e;
        }
    }

    public List<T> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<T> query = session.createQuery("from " + type.getName(), type);
            List<T> result = query.list();

            logger.info("Loaded {} records from {}",  result.size(), type.getSimpleName());

            return result;
        } catch (Exception e) {
            logger.error("Failed to load {}", type.getSimpleName(), e);
            throw e;
        }
    }

    public void delete(T entity) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.remove(session.contains(entity) ? entity : session.merge(entity));

            transaction.commit();

            logger.info("Deleted {}", type.getSimpleName());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Failed to delete {}", type.getSimpleName(), e);
            throw e;
        }
    }
}