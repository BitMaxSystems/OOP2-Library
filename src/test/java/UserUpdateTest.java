import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdatePasswordChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdateUserChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;


import static org.junit.jupiter.api.Assertions.*;

public class UserUpdateTest {
    private GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);

    @BeforeAll
    static void setup()
    {
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> createUserChain.execute(formDTO));

    }

    @BeforeEach
    void resetChanges()
    {
        User user = userGenericRepository.findAll().stream()
                .filter(u -> u.getLastName().equals("Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(user);
        user.setFirstName("Test");

        assertDoesNotThrow(() -> userGenericRepository.update(user));
    }

    @Test
    void testUserDataUpdate()
    {
        String beforeFirstName;
        UserRole beforeRole;

        User user = userGenericRepository.findAll().stream()
                .filter(u -> u.getLastName().equals("Test") &&  u.getFirstName().equals("Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(user);

        beforeFirstName = user.getFirstName();
        beforeRole = user.getRole();

        UserDataDTO formDataDTo = new UserDataDTO.Builder("Test2",
                user.getLastName(),
                String.valueOf(user.getAge()),
                user.getPhone(),
                user.getCredentials().getUsername())
                .setLoyaltyPoints(String.valueOf(user.getLoyaltyPoints()))
                .setRole(UserRole.ADMINISTRATOR)
                .setUser(user).build();

        IUserFormChain updateUser = new UpdateUserChain();

        assertDoesNotThrow(() -> updateUser.execute(formDataDTo));


        assertNotEquals(user.getFirstName(),beforeFirstName);
        assertNotEquals(user.getRole(),beforeRole);

        assertEquals("Test2",user.getFirstName());
        assertEquals(UserRole.ADMINISTRATOR,user.getRole());
    }

    @Test
    void testUserCredentialsUpdate()
    {
        String beforeHashPassword, afterHashPassword;

        User user = userGenericRepository.findAll().stream()
                .filter(u -> u.getFirstName().equals("Test") &&  u.getLastName().equals("Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(user);

        beforeHashPassword = user.getCredentials().getPassword();

        assertTrue(BCrypt.checkpw("TestTest!123",beforeHashPassword));

        UserDataDTO formDataDTo = new UserDataDTO.Builder(user.getFirstName(),
                user.getLastName(),
                String.valueOf(user.getAge()),
                user.getPhone(),
                user.getCredentials().getUsername())
                .setLoyaltyPoints(String.valueOf(user.getLoyaltyPoints()))
                .setRole(user.getRole())
                .setUser(user)
                .setNewPassword("TestTest!321","TestTest!321").build();

        IUserFormChain updatePassword = new UpdatePasswordChain();

        assertDoesNotThrow(() -> updatePassword.execute(formDataDTo));

        afterHashPassword = user.getCredentials().getPassword();

        assertFalse(BCrypt.checkpw("TestTest!123",afterHashPassword));
        assertTrue(BCrypt.checkpw("TestTest!321",afterHashPassword));
    }
}
