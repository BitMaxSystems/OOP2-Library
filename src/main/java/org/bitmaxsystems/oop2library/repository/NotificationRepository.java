package org.bitmaxsystems.oop2library.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class NotificationRepository {

    private static NotificationRepository repository;
    private static final Logger logger = LogManager.getLogger(NotificationRepository.class);

    private NotificationRepository() {
    }

    public static NotificationRepository getInstance() {
        if (repository == null) {
            repository = new NotificationRepository();
        }

        return repository;
    }

    public void save(Notification notification) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.persist(notification);

            transaction.commit();

            logger.info("Saved notification of type {}", notification.getType());
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            logger.error("Failed to save notification of type {}", notification.getType(), e);
            throw e;
        }
    }

    public List<Notification> getAdminNotifications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notification> query = session.createQuery(
                    "FROM Notification WHERE audience IN (:adminAudience, :staffAudience) ORDER BY timestamp DESC",
                    Notification.class
            );

            query.setParameter("adminAudience", NotificationAudience.ADMIN);
            query.setParameter("staffAudience", NotificationAudience.STAFF);

            List<Notification> notifications = query.list();

            logger.info("Loaded {} admin activity notifications", notifications.size());

            return notifications;
        } catch (Exception e) {
            logger.error("Failed to load admin activity notifications", e);
            throw e;
        }
    }

    public List<Notification> getLibrarianNotifications() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Notification> query = session.createQuery(
                    "FROM Notification WHERE audience = :audience ORDER BY timestamp DESC",
                    Notification.class
            );

            query.setParameter("audience", NotificationAudience.STAFF);

            List<Notification> notifications = query.list();

            logger.info("Loaded {} librarian activity notifications", notifications.size());

            return notifications;
        } catch (Exception e) {
            logger.error("Failed to load librarian activity notifications", e);
            throw e;
        }
    }
}