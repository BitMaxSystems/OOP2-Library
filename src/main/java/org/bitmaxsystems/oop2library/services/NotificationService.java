package org.bitmaxsystems.oop2library.services;

import org.bitmaxsystems.oop2library.models.dto.NotificationDTO;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.HistoryRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NotificationService {

    private final HistoryRepository historyRepository = HistoryRepository.getInstance();
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public List<NotificationDTO> getUserNotifications(User user) {
        List<History> activeHistory = historyRepository.getActiveHistoryByUser(user);

        return createLendingNotifications(activeHistory, false);
    }

    public List<NotificationDTO> getLibrarianNotifications() {
        List<NotificationDTO> notifications = new ArrayList<>();

        notifications.addAll(
                createLendingNotifications(
                        historyRepository.getActiveHistory(),
                        true
                )
        );

        notifications.addAll(
                convertActivityNotifications(
                        notificationRepository.getLibrarianNotifications()
                )
        );

        sortNotifications(notifications);

        return notifications;
    }

    public List<NotificationDTO> getAdminNotifications() {
        List<NotificationDTO> notifications = new ArrayList<>();

        notifications.addAll(
                createLendingNotifications(
                        historyRepository.getActiveHistory(),
                        true
                )
        );

        notifications.addAll(
                convertActivityNotifications(
                        notificationRepository.getAdminNotifications()
                )
        );

        sortNotifications(notifications);

        return notifications;
    }

    private List<NotificationDTO> createLendingNotifications(
            List<History> historyList,
            boolean includeUserInformation
    ) {
        List<NotificationDTO> notifications = new ArrayList<>();

        for (History history : historyList) {
            if (history.getExpectedReturnDate().isEqual(LocalDate.now())) {
                notifications.add(
                        createDueTodayNotification(
                                history,
                                includeUserInformation
                        )
                );
            } else if (history.getLendStatus() == LendStatusEnum.OVERDUE) {
                notifications.add(
                        createOverdueNotification(
                                history,
                                includeUserInformation
                        )
                );
            }
        }

        return notifications;
    }

    private NotificationDTO createDueTodayNotification(
            History history,
            boolean includeUserInformation
    ) {
        String bookTitle = history.getInventory().getBook().getTitle();

        String message;

        if (includeUserInformation) {
            message = bookTitle + " borrowed by "
                    + history.getUser().getFirstName() + " "
                    + history.getUser().getLastName()
                    + " is due today.";
        } else {
            message = bookTitle + " is due today.";
        }

        return new NotificationDTO(
                "Book due today",
                message,
                NotificationType.DUE_TODAY,
                LocalDateTime.now()
        );
    }

    private NotificationDTO createOverdueNotification(
            History history,
            boolean includeUserInformation
    ) {
        String bookTitle = history.getInventory().getBook().getTitle();

        String message;

        if (includeUserInformation) {
            message = bookTitle + " borrowed by "
                    + history.getUser().getFirstName() + " "
                    + history.getUser().getLastName()
                    + " is overdue. Expected return date: "
                    + history.getExpectedReturnDate() + ".";
        } else {
            message = bookTitle + " is overdue. Expected return date: "
                    + history.getExpectedReturnDate() + ".";
        }

        return new NotificationDTO(
                "Overdue book",
                message,
                NotificationType.OVERDUE,
                LocalDateTime.now()
        );
    }

    private List<NotificationDTO> convertActivityNotifications(
            List<Notification> activityNotifications
    ) {
        return activityNotifications.stream()
                .map(notification -> new NotificationDTO(
                        notification.getTitle(),
                        notification.getMessage(),
                        notification.getType(),
                        notification.getTimestamp()
                ))
                .toList();
    }

    private void sortNotifications(List<NotificationDTO> notifications) {
        notifications.sort(
                Comparator.comparing(
                        NotificationDTO::getTimestamp
                ).reversed()
        );
    }
}