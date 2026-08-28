package org.bitmaxsystems.oop2library.services;

import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

public class DeleteUserService {
    private final GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public void deleteUser(User user) {
        String userName = user.getFirstName() + " " + user.getLastName();

        userGenericRepository.delete(user);

        Notification notification = new Notification(
                "User deleted",
                userName + " was deleted.",
                NotificationType.USER_UPDATED,
                NotificationAudience.ADMIN
        );

        notificationRepository.save(notification);
    }
}