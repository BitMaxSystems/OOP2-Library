import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.NotificationDTO;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.HistoryRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.services.NotificationService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class NotificationServiceTest {

    private NotificationService notificationService;
    private HistoryRepository historyRepository;
    private NotificationRepository notificationRepository;

    private GenericRepository<User> userRepository;
    private GenericRepository<Book> bookRepository;
    private GenericRepository<Inventory> inventoryRepository;
    private GenericRepository<Author> authorRepository;
    private GenericRepository<Genre> genreRepository;
    private GenericRepository<Publisher> publisherRepository;

    private User reader;
    private User secondReader;
    private Book book;
    private Inventory inventory;

    @BeforeEach
    void setUp() {
        clearDatabase();

        notificationService = new NotificationService();
        historyRepository = HistoryRepository.getInstance();
        notificationRepository = NotificationRepository.getInstance();

        userRepository = new GenericRepository<>(User.class);
        bookRepository = new GenericRepository<>(Book.class);
        inventoryRepository = new GenericRepository<>(Inventory.class);
        authorRepository = new GenericRepository<>(Author.class);
        genreRepository = new GenericRepository<>(Genre.class);
        publisherRepository = new GenericRepository<>(Publisher.class);

        reader = new User.Builder(
                "John",
                "Reader",
                25,
                "0888111222",
                UserRole.READER
        ).setDateOfApproval(LocalDate.now()).build();

        secondReader = new User.Builder(
                "Jane",
                "Reader",
                28,
                "0888333444",
                UserRole.READER
        ).setDateOfApproval(LocalDate.now()).build();

        userRepository.save(reader);
        userRepository.save(secondReader);

        Author author = new Author("Test Author");
        Genre genre = new Genre("Test Genre");
        Publisher publisher = new Publisher("Test Publisher");

        authorRepository.save(author);
        genreRepository.save(genre);
        publisherRepository.save(publisher);

        book = new Book(
                "9780000000001",
                "Test Book",
                author,
                genre,
                publisher
        );

        bookRepository.save(book);

        inventory = new Inventory(book);
        inventoryRepository.save(inventory);
    }

    @Test
    void readerReceivesDueTodayNotificationForOwnBook() {
        History history = new History.Builder(reader, inventory)
                .lendInside()
                .build();

        historyRepository.saveHistory(inventory, history);

        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(reader);

        assertEquals(1, notifications.size());

        NotificationDTO notification = notifications.getFirst();

        assertEquals(NotificationType.DUE_TODAY, notification.getType());
        assertEquals("Book due today", notification.getTitle());
        assertTrue(notification.getMessage().contains("Test Book"));
        assertFalse(notification.getMessage().contains("John Reader"));
    }

    @Test
    void readerDoesNotReceiveAnotherReadersNotification() {
        History history = new History.Builder(secondReader, inventory)
                .lendInside()
                .build();

        historyRepository.saveHistory(inventory, history);

        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(reader);

        assertTrue(notifications.isEmpty());
    }

    @Test
    void returnedBookDoesNotGenerateDueNotification() {
        History history = new History.Builder(reader, inventory)
                .lendInside()
                .build();

        history.setActualReturnDate(LocalDate.now());

        historyRepository.saveHistory(inventory, history);

        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(reader);

        assertTrue(notifications.isEmpty());
    }

    @Test
    void readerReceivesOverdueNotification() {
        History history = new History.Builder(reader, inventory)
                .lendOutside()
                .build();

        historyRepository.saveHistory(inventory, history);

        makeHistoryOverdue(history);

        List<NotificationDTO> notifications =
                notificationService.getUserNotifications(reader);

        assertEquals(1, notifications.size());

        NotificationDTO notification = notifications.getFirst();

        assertEquals(NotificationType.OVERDUE, notification.getType());
        assertEquals("Overdue book", notification.getTitle());
        assertTrue(notification.getMessage().contains("Test Book"));
        assertTrue(
                notification.getMessage().contains(
                        LocalDate.now().minusDays(1).toString()
                )
        );
    }

    @Test
    void librarianReceivesDueTodayNotificationWithReaderInformation() {
        History history = new History.Builder(reader, inventory)
                .lendInside()
                .build();

        historyRepository.saveHistory(inventory, history);

        List<NotificationDTO> notifications =
                notificationService.getLibrarianNotifications();

        assertEquals(1, notifications.size());

        NotificationDTO notification = notifications.getFirst();

        assertEquals(NotificationType.DUE_TODAY, notification.getType());
        assertTrue(notification.getMessage().contains("Test Book"));
        assertTrue(notification.getMessage().contains("John Reader"));
    }

    @Test
    void librarianReceivesStaffActivityNotification() {
        notificationRepository.save(
                new Notification(
                        "Book returned",
                        "Test Book was returned.",
                        NotificationType.BOOK_RETURNED,
                        NotificationAudience.STAFF
                )
        );

        List<NotificationDTO> notifications =
                notificationService.getLibrarianNotifications();

        assertEquals(1, notifications.size());
        assertEquals(
                NotificationType.BOOK_RETURNED,
                notifications.getFirst().getType()
        );
    }

    @Test
    void librarianDoesNotReceiveAdminActivityNotification() {
        notificationRepository.save(
                new Notification(
                        "User updated",
                        "A user was updated.",
                        NotificationType.USER_UPDATED,
                        NotificationAudience.ADMIN
                )
        );

        List<NotificationDTO> notifications =
                notificationService.getLibrarianNotifications();

        assertTrue(notifications.isEmpty());
    }

    @Test
    void adminReceivesStaffAndAdminActivityNotifications() {
        notificationRepository.save(
                new Notification(
                        "Book returned",
                        "A book was returned.",
                        NotificationType.BOOK_RETURNED,
                        NotificationAudience.STAFF
                )
        );

        notificationRepository.save(
                new Notification(
                        "User updated",
                        "A user was updated.",
                        NotificationType.USER_UPDATED,
                        NotificationAudience.ADMIN
                )
        );

        List<NotificationDTO> notifications =
                notificationService.getAdminNotifications();

        assertEquals(2, notifications.size());

        assertTrue(
                notifications.stream()
                        .anyMatch(notification ->
                                notification.getType()
                                        == NotificationType.BOOK_RETURNED)
        );

        assertTrue(
                notifications.stream()
                        .anyMatch(notification ->
                                notification.getType()
                                        == NotificationType.USER_UPDATED)
        );
    }

    @Test
    void adminReceivesDueNotificationWithReaderInformation() {
        History history = new History.Builder(reader, inventory)
                .lendInside()
                .build();

        historyRepository.saveHistory(inventory, history);

        List<NotificationDTO> notifications =
                notificationService.getAdminNotifications();

        assertEquals(1, notifications.size());

        NotificationDTO notification = notifications.getFirst();

        assertEquals(NotificationType.DUE_TODAY, notification.getType());
        assertTrue(notification.getMessage().contains("John Reader"));
    }

    private void makeHistoryOverdue(History history) {
        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.createMutationQuery(
                            "UPDATE History " +
                                    "SET expectedReturnDate = :date " +
                                    "WHERE id = :id"
                    )
                    .setParameter(
                            "date",
                            LocalDate.now().minusDays(1)
                    )
                    .setParameter("id", history.getId())
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw e;
        }
    }

    private void clearDatabase() {
        Transaction transaction = null;

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            transaction = session.beginTransaction();

            session.createMutationQuery("DELETE FROM History")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Notification")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Inventory")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Book")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Author")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Genre")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM Publisher")
                    .executeUpdate();

            session.createMutationQuery("DELETE FROM User")
                    .executeUpdate();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }

            throw e;
        }
    }
}