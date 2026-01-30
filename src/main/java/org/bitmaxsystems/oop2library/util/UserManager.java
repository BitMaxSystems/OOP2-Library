package org.bitmaxsystems.oop2library.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.AuthorisationRepository;
import org.bitmaxsystems.oop2library.repository.GenericRepository;


public class UserManager {
    private User loggedUser = null;
    private final AuthorisationRepository authorisationRepository = new AuthorisationRepository();
    private GenericRepository<User> userRepository = new GenericRepository<>(User.class);
    private static final Logger logger = LogManager.getLogger(UserManager.class);
    private static final UserManager manager = new UserManager();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return manager;
    }

    public void login(String username, String password) throws DataValidationException, SecurityException
    {
        if (loggedUser != null)
        {
            throw new SecurityException("User already logged in");
        }

        Credentials userCredentials = authorisationRepository.getUserAuthorisation(username);
        if (userCredentials == null) {
            logger.error("{} not found in the database", username);
            throw new DataValidationException("Invalid credentials");
        } else {
            if (userCredentials.equals(username, password)) {
                logger.info("{} logged successfully",username);
                loggedUser = userCredentials.getUser();
            } else {
                logger.error("{} inputted wrong password", username);
                throw new DataValidationException("Invalid credentials");
            }
        }
    }

    public void refreshLoggedUserData()
    {
        this.loggedUser = userRepository.findById(loggedUser.getId());
    }

    public void logoff()
    {
        loggedUser = null;
    }

    public User getLoggedUser()
    {
        return loggedUser;
    }
}
