package org.bitmaxsystems.oop2library.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteUser {
    private static final Logger logger = LogManager.getLogger(DeleteUser.class);
    private GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);

    public void deleteUser(User user)
    {
        userGenericRepository.delete(user);
    }
}
