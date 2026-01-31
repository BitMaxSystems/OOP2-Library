import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.AuthorisationRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagementTest {

    private  AuthorisationRepository authorisationRepository = new AuthorisationRepository();
    private UserManager manager = UserManager.getInstance();

    @BeforeAll
    static void setup()
    {
        HibernateInit.initializeIfEmpty();
    }

    @AfterEach
    void resetManager() {UserManager.getInstance().logoff();}

    @Test
    void userNotFoundException()
    {
        String username = "test";
        String password = "admin";

        DataValidationException exception =  assertThrowsExactly(DataValidationException.class,
                () -> manager.login(username,password));
        assertEquals("Invalid credentials",exception.getMessage());

    }

    @Test
    void wrongPasswordException()
    {
        String username = "admin";
        String password = "test";

        DataValidationException exception =  assertThrowsExactly(DataValidationException.class,
                () -> manager.login(username,password));
        assertEquals("Invalid credentials",exception.getMessage());

    }

    @Test
    void alreadyLoggedInException()
    {
        String username = "admin";
        String password = "admin";

        assertDoesNotThrow(() -> manager.login(username,password));
        SecurityException exception = assertThrowsExactly(SecurityException.class,() -> manager.login(username,password));
        assertEquals("User already logged in",exception.getMessage());
    }

    @Test
    void testLogin()
    {
        String username = "admin";
        String password = "admin";

       assertDoesNotThrow(() -> manager.login(username,password));
       assertNotNull(manager.getLoggedUser());

       User user = authorisationRepository.getUserAuthorisation(username).getUser();

        assertEquals(manager.getLoggedUser(), user);
    }

    @Test
    void testLogout()
    {
        assertDoesNotThrow(manager::logoff);
        assertNull(manager.getLoggedUser());
    }
}
