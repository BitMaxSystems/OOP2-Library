package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.HistoryRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LibraryService {

    private final HistoryRepository historyRepository = HistoryRepository.getInstance();
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    private static final Logger logger = LogManager.getLogger(LibraryService.class);

    public void lendOutside(User user, Inventory inventory) {
        if (user.getLoyaltyPoints() < 30) {
            throw new IllegalStateException(
                    user.getFirstName() + " " + user.getLastName()
                            + " has insufficient loyalty points to lend "
                            + inventory.getBook().getTitle()
                            + " outside the library!"
            );
        }

        inventory.lendOutside();

        History history = new History.Builder(user, inventory)
                .lendOutside()
                .build();

        historyRepository.saveHistory(inventory,history);

        Notification notification = new Notification(
                "Book lent",
                inventory.getBook().getTitle()
                        + " was lent outside to "
                        + user.getFirstName() + " "
                        + user.getLastName() + ".",
                NotificationType.BOOK_LENT,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);

        logger.info(
                "Inventory book: {} - {} was successfully lent outside for user: {} {}",
                inventory.getId(),
                inventory.getBook().getTitle(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    public void lendInside(User user, Inventory inventory) {
        try {
            inventory.lendInside();

            History history = new History.Builder(user, inventory)
                    .lendInside()
                    .build();

            historyRepository.saveHistory(inventory,history);

            Notification notification = new Notification(
                    "Book lent",
                    inventory.getBook().getTitle()
                            + " was lent inside to "
                            + user.getFirstName() + " "
                            + user.getLastName() + ".",
                    NotificationType.BOOK_LENT,
                    NotificationAudience.STAFF
            );

            notificationRepository.save(notification);

            logger.info(
                    "Inventory book: {} - {} was successfully lent inside for user: {} {}",
                    inventory.getId(),
                    inventory.getBook().getTitle(),
                    user.getFirstName(),
                    user.getLastName()
            );
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public void returnBook(History history, Boolean needsArchiving) {
        long timeDiff;
        int calculatedLoyaltyPoints;
        User user = history.getUser();
        Inventory inventory = history.getInventory();

        history.setActualReturnDate(LocalDate.now());
        inventory.returnBook();

        if (needsArchiving != null) {
            inventory.setNeedsArchiving(needsArchiving);

            if (needsArchiving) {
                logger.info(
                        "Inventory copy {} of book '{}' was marked as needing archiving after return",
                        inventory.getId(),
                        inventory.getBook().getTitle()
                );
            } else {
                logger.info(
                        "Inventory copy {} of book '{}' was checked and does not need archiving after return",
                        inventory.getId(),
                        inventory.getBook().getTitle()
                );
            }
        }

        timeDiff = ChronoUnit.DAYS.between(
                history.getActualReturnDate(),
                history.getExpectedReturnDate()
        );

        if (timeDiff >= 0) {
            calculatedLoyaltyPoints = 2 + (Math.toIntExact(timeDiff) * 2);
        } else {
            calculatedLoyaltyPoints = Math.toIntExact(timeDiff) * 5;
        }

        user.updateLoyaltyPoints(calculatedLoyaltyPoints);

        historyRepository.updateHistory(inventory,user,history);

        Notification notification = new Notification(
                "Book returned",
                inventory.getBook().getTitle()
                        + " was returned by "
                        + user.getFirstName() + " "
                        + user.getLastName() + ".",
                NotificationType.BOOK_RETURNED,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);

        if (needsArchiving != null) {
            Notification archiveNotification = getArchiveNotification(needsArchiving, inventory);

            notificationRepository.save(archiveNotification);
        }


        logger.info(
                "Inventory book: {} - {} was successfully returned by user: {} {}",
                inventory.getId(),
                inventory.getBook().getTitle(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private static Notification getArchiveNotification(Boolean needsArchiving, Inventory inventory) {
        Notification archiveNotification;

        if (needsArchiving) {
            archiveNotification = new Notification(
                    "Book needs archiving",
                    "Inventory copy #" + inventory.getId()
                            + " of " + inventory.getBook().getTitle()
                            + " was marked for archiving after return.",
                    NotificationType.INVENTORY_UPDATED,
                    NotificationAudience.STAFF
            );
        } else {
            archiveNotification = new Notification(
                    "Book does not need archiving",
                    "Inventory copy #" + inventory.getId()
                            + " of " + inventory.getBook().getTitle()
                            + " does not need archiving after return.",
                    NotificationType.INVENTORY_UPDATED,
                    NotificationAudience.STAFF
            );
        }
        return archiveNotification;
    }
}