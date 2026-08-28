import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationRepositoryTest {

    private final NotificationRepository notificationRepository =
            NotificationRepository.getInstance();

    @BeforeEach
    void clearNotifications() {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            session.createMutationQuery("DELETE FROM Notification")
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw e;
        }
    }

    @Test
    void saveNotification() {
        Notification notification = new Notification(
                "Test notification",
                "Test message",
                NotificationType.INVENTORY_UPDATED,
                NotificationAudience.STAFF
        );

        assertDoesNotThrow(() ->
                notificationRepository.save(notification)
        );

        List<Notification> notifications =
                notificationRepository.getLibrarianNotifications();

        assertEquals(1, notifications.size());
        assertEquals("Test notification", notifications.getFirst().getTitle());
        assertEquals("Test message", notifications.getFirst().getMessage());
        assertEquals(
                NotificationType.INVENTORY_UPDATED,
                notifications.getFirst().getType()
        );
        assertEquals(
                NotificationAudience.STAFF,
                notifications.getFirst().getAudience()
        );
        assertNotNull(notifications.getFirst().getTimestamp());
    }

    @Test
    void librarianReceivesStaffNotifications() {
        notificationRepository.save(
                new Notification(
                        "Staff notification",
                        "Visible to staff",
                        NotificationType.INVENTORY_UPDATED,
                        NotificationAudience.STAFF
                )
        );

        List<Notification> notifications =
                notificationRepository.getLibrarianNotifications();

        assertEquals(1, notifications.size());
        assertEquals(
                NotificationAudience.STAFF,
                notifications.getFirst().getAudience()
        );
    }

    @Test
    void librarianDoesNotReceiveAdminNotifications() {
        notificationRepository.save(
                new Notification(
                        "Admin notification",
                        "Visible only to admins",
                        NotificationType.USER_UPDATED,
                        NotificationAudience.ADMIN
                )
        );

        List<Notification> notifications =
                notificationRepository.getLibrarianNotifications();

        assertTrue(notifications.isEmpty());
    }

    @Test
    void adminReceivesAdminNotifications() {
        notificationRepository.save(
                new Notification(
                        "Admin notification",
                        "Visible only to admins",
                        NotificationType.USER_UPDATED,
                        NotificationAudience.ADMIN
                )
        );

        List<Notification> notifications =
                notificationRepository.getAdminNotifications();

        assertEquals(1, notifications.size());
        assertEquals(
                NotificationAudience.ADMIN,
                notifications.getFirst().getAudience()
        );
    }

    @Test
    void adminReceivesStaffNotifications() {
        notificationRepository.save(
                new Notification(
                        "Staff notification",
                        "Visible to all staff",
                        NotificationType.BOOK_RETURNED,
                        NotificationAudience.STAFF
                )
        );

        List<Notification> notifications =
                notificationRepository.getAdminNotifications();

        assertEquals(1, notifications.size());
        assertEquals(
                NotificationAudience.STAFF,
                notifications.getFirst().getAudience()
        );
    }

    @Test
    void adminReceivesBothAdminAndStaffNotifications() {
        notificationRepository.save(
                new Notification(
                        "Admin notification",
                        "Admin activity",
                        NotificationType.USER_UPDATED,
                        NotificationAudience.ADMIN
                )
        );

        notificationRepository.save(
                new Notification(
                        "Staff notification",
                        "Staff activity",
                        NotificationType.BOOK_RETURNED,
                        NotificationAudience.STAFF
                )
        );

        List<Notification> notifications =
                notificationRepository.getAdminNotifications();

        assertEquals(2, notifications.size());

        assertTrue(
                notifications.stream()
                        .anyMatch(notification ->
                                notification.getAudience()
                                        == NotificationAudience.ADMIN)
        );

        assertTrue(
                notifications.stream()
                        .anyMatch(notification ->
                                notification.getAudience()
                                        == NotificationAudience.STAFF)
        );
    }
}