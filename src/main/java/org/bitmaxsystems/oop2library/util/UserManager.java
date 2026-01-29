package org.bitmaxsystems.oop2library.util;

import org.bitmaxsystems.oop2library.models.users.User;

public class UserManager {
    private User loggedUser = null;
    private static final UserManager service = new UserManager();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return service;
    }

    public void login(User user)
    {
        loggedUser = user;
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
