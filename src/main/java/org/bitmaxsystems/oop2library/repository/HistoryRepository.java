package org.bitmaxsystems.oop2library.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HistoryRepository {

    private static HistoryRepository repository = null;
    private static final Logger logger = LogManager.getLogger(HistoryRepository.class);

    private HistoryRepository() {
    }

    public static HistoryRepository getInstance() {
        if (repository == null) {
            repository = new HistoryRepository();
        }

        return repository;
    }

    public List<History> getActiveHistory() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery(
                    "FROM History WHERE actualReturnDate IS NULL",
                    History.class
            );

            List<History> history = query.list();

            logger.info("Loaded {} active lend history records", history.size());

            return history;
        } catch (Exception e) {
            logger.error("Failed to load active lend history", e);
            throw e;
        }
    }

    public List<History> getActiveHistoryByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery(
                    "FROM History WHERE user = :user AND actualReturnDate IS NULL",
                    History.class
            );

            query.setParameter("user", user);

            List<History> history = query.list();

            logger.info(
                    "Loaded {} active lend history records for {} {}",
                    history.size(),
                    user.getFirstName(),
                    user.getLastName()
            );

            return history;
        } catch (Exception e) {
            logger.error(
                    "Failed to load active lend history for {} {}",
                    user.getFirstName(),
                    user.getLastName(),
                    e
            );

            throw e;
        }
    }

    public List<History> searchByUser(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History WHERE user = :user", History.class);
            query.setParameter("user", user);
            List<History> history = query.list();

            logger.info(
                    "Loaded {} lend history records for {} {}",
                    history.size(),
                    user.getFirstName(),
                    user.getLastName()
            );

            return history;

        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public void saveHistory(Inventory inventory, History history)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(inventory);
            session.persist(history);
            transaction.commit();
            logger.info("Saved lend history for inventory copy {}", inventory.getId());
        } catch (Exception e) {
            logger.error("Failed to save lend history for inventory copy {}", inventory.getId(), e);
            throw e;
        }
    }

    public void updateHistory(Inventory inventory, User user ,History history)
    {
        Transaction transaction;
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(inventory);
            session.merge(user);
            session.merge(history);
            transaction.commit();
            logger.info("Updated lend history for inventory copy {}", inventory.getId());
        } catch (Exception e) {
            logger.error("Failed to update lend history for inventory copy {}", inventory.getId(), e);
            throw e;
        }
    }

}
