import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.exceptions.UserAlreadyExistException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.SaveFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserFormTest {

    private GenericRepository<User> userRepository = new GenericRepository<>(User.class);
    private GenericRepository<UserForm> formRepository = new GenericRepository<>(UserForm.class);

    @BeforeAll
    static void setup()
    {HibernateInit.initializeIfEmpty();}



    @Test
    void UserAlreadyExistsException()
    {
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "admin")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        UserAlreadyExistException exception = assertThrowsExactly(UserAlreadyExistException.class,() -> createUserChain.execute(formDTO));
        assertEquals("- A user with this username already exists!",exception.getMessage());
    }

    @Test
    void testCreateUser()
    {
        int before = userRepository.findAll().size();
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> createUserChain.execute(formDTO));

        int after = userRepository.findAll().size();

        assertTrue(after>before);
    }

    @Test
    void testNewForm()
    {
        int beforeUser = userRepository.findAll().size();
        int beforeForm = formRepository.findAll().size();
        IUserFormChain verifyDataChain = new VerifyDataChain();
        IUserFormChain createUserChain = new CreateUserChain();
        IUserFormChain saveFormChain = new SaveFormChain();

        verifyDataChain.setNextChain(createUserChain);
        createUserChain.setNextChain(saveFormChain);

        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test1")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> verifyDataChain.execute(formDTO));

        int afterUser = userRepository.findAll().size();
        int afterForm = formRepository.findAll().size();

        assertTrue(afterUser>beforeUser);
        assertTrue(afterForm>beforeForm);
    }
}
