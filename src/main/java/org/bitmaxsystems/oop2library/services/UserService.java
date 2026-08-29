package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.util.chain.userform.UpdatePasswordChain;
import org.bitmaxsystems.oop2library.util.chain.userform.UpdateUserChain;
import org.bitmaxsystems.oop2library.util.chain.userform.VerifyUserDataChain;
import org.bitmaxsystems.oop2library.util.chain.userform.contract.IUserFormChain;

import java.util.List;

public class UserService {
    private final UserRepository userRepository = UserRepository.getInstance();
    private static final Logger logger = LogManager.getLogger(UserService.class);
    private final GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public List<User> getUsersByRole(UserRole role)
    {
        try
        {
          return userRepository.searchByRole(role);
        }
        catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public List<User> getAllApprovedReaders()
    {
        try {

            List<User> readersList = userRepository.searchByRole(UserRole.READER);
            logger.info("Loaded all approved users");
            return readersList;
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public void updateUser(UserDataDTO form) throws Exception {
        IUserFormChain verifyUser = new VerifyUserDataChain();
        IUserFormChain updateUser = new UpdateUserChain();
        IUserFormChain updatePassword = new UpdatePasswordChain();

        verifyUser.setNextChain(updateUser);
        updateUser.setNextChain(updatePassword);


        try {
            verifyUser.execute(form);
            logger.info("User data successfully submitted!");

        }
        catch (Exception e)
        {
            logger.error(e);
            throw e;
        }

    }

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
