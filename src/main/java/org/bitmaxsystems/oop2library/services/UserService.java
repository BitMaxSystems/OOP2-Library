package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.controllers.LibraryHistoryController;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.UserRepository;

import java.util.List;

public class UserService {
    private final UserRepository userRepository = UserRepository.getInstance();
    private static final Logger logger = LogManager.getLogger(UserService.class);



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
}
