import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;

import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.AuthorisationRepository;

import org.bitmaxsystems.oop2library.util.UserManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {

    private  AuthorisationRepository authorisationRepository = new AuthorisationRepository();

    @BeforeAll
    static void setup()
    {
        HibernateInit.initializeIfEmpty();
    }

    @Test
    void userNotFoundException()
    {
        String username = "test";
        String password = "admin";
        UserManager manager = UserManager.getInstance();

        DataValidationException exception =  assertThrowsExactly(DataValidationException.class,
                () -> manager.login(username,password));
        assertEquals("Invalid credentials",exception.getMessage());

    }

    @Test
    void wrongPasswordException()
    {
        String username = "admin";
        String password = "test";
        UserManager manager = UserManager.getInstance();

        DataValidationException exception =  assertThrowsExactly(DataValidationException.class,
                () -> manager.login(username,password));
        assertEquals("Invalid credentials",exception.getMessage());
        manager.logoff();

    }

    @Test
    void alreadyLoggedInException()
    {
        String username = "admin";
        String password = "admin";
        UserManager manager = UserManager.getInstance();

        assertDoesNotThrow(() -> manager.login(username,password));
        SecurityException exception = assertThrowsExactly(SecurityException.class,() -> manager.login(username,password));
        assertEquals("User already logged in",exception.getMessage());
        manager.logoff();
    }

    @Test
    void testLogin()
    {
        String username = "admin";
        String password = "admin";
        UserManager manager = UserManager.getInstance();

       assertDoesNotThrow(() -> manager.login(username,password));
       assertNotNull(manager.getLoggedUser());

       User user = authorisationRepository.getUserAuthorisation(username).getUser();

       assertEquals(user.getId(),manager.getLoggedUser().getId());
       manager.logoff();
    }

    @Test
    void testLogout()
    {
        UserManager manager = UserManager.getInstance();

        assertDoesNotThrow(manager::logoff);
        assertNull(manager.getLoggedUser());

    }
}
