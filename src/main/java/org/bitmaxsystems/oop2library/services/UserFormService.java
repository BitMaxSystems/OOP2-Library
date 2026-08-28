package org.bitmaxsystems.oop2library.services;

import org.bitmaxsystems.oop2library.exceptions.FormAlreadyApprovedException;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

public class UserFormService {
    private final GenericRepository<UserForm> userFormGenericRepository = new GenericRepository<>(UserForm.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public void approveUser(UserForm form) throws FormAlreadyApprovedException {
        if (!form.approveForm()) {
            throw new FormAlreadyApprovedException("This user is already approved!");
        }

        userFormGenericRepository.update(form);

        Notification notification = new Notification(
                "User approved",
                "A user registration form was approved.",
                NotificationType.USER_UPDATED,
                NotificationAudience.ADMIN
        );

        notificationRepository.save(notification);
    }
}