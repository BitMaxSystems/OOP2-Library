package org.bitmaxsystems.oop2library.util;

public class UserManager {
    private boolean loggedUser = false;
    private static final UserManager service = new UserManager();

    private UserManager() {
    }

    public static UserManager getInstance() {
        return service;
    }

    public void login()
    {
        loggedUser = true;
    }

    public void logoff()
    {
        loggedUser = false;
    }

    public boolean isLoggedUser()
    {
        return loggedUser;
    }
}
