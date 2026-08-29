package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.controllers.UserFormController;
import org.bitmaxsystems.oop2library.exceptions.FormAlreadyApprovedException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.util.chain.userform.CreateUserChain;
import org.bitmaxsystems.oop2library.util.chain.userform.VerifyUserDataChain;
import org.bitmaxsystems.oop2library.util.chain.userform.contract.IUserFormChain;

import java.util.List;
import java.util.Objects;

public class UserFormService {
    private final GenericRepository<UserForm> userFormGenericRepository = new GenericRepository<>(UserForm.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();
    private static final Logger logger = LogManager.getLogger(UserFormService.class);
    private IUserFormChain executableChain;

    public void approveUser(UserForm form) {
        try {
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

            User user = form.getUser();
            logger.info("{} {} successfully approved", user.getFirstName(), user.getLastName());

        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public List<UserForm> getAllUserForms()
    {
        try {
            return userFormGenericRepository.findAll();
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public void setUpChain(IUserFormChain additionalChain)
    {
        IUserFormChain verifyData = new VerifyUserDataChain();
        IUserFormChain createUser = new CreateUserChain();

        verifyData.setNextChain(createUser);

        if (Objects.nonNull(additionalChain))
        {
            createUser.setNextChain(additionalChain);
        }

        executableChain = verifyData;
    }

    public void executeChain(UserDataDTO form) throws Exception {
        try {
            if (Objects.isNull(executableChain))
            {
                throw new Exception("Chain is not set up");
            }

            executableChain.execute(form);
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
}