package org.bitmaxsystems.oop2library.util.chain.userform;

import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.util.chain.userform.contract.IUserFormChain;

import java.util.Objects;

public class SendFormNotificationChain implements IUserFormChain {
    private IUserFormChain nextChain;
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    @Override
    public void setNextChain(IUserFormChain chain) {
        this.nextChain = chain;
    }

    @Override
    public void execute(UserDataDTO formData) throws Exception {

        String message = "New unapproved reader "+ formData.getFirstName() + " " + formData.getLastName()+
                " submitted a new user form!";

        Notification notification = new Notification(
                "New user form",
                message,
                NotificationType.NEW_USER_FORM,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);

        if (Objects.nonNull(nextChain))
        {
            nextChain.execute(formData);
        }

    }
}
