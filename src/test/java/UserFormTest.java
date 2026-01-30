import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.exceptions.UserAlreadyExistException;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.form.UserFormDTO;
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
    void firstNameExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("213",
                "Test",
                "14",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- First name can only be word characters without whitespaces!\n",exception.getMessage());
    }

    @Test
    void lastNameExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "132",
                "14",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Last name can only be word characters without whitespaces!\n",exception.getMessage());
    }


    @Test
    void ageNonNumericExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "das",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Age must be a numeric value!\n",exception.getMessage());
    }

    @Test
    void ageOutOfIntervalExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "-2",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Age must be between 13 and 100!\n",exception.getMessage());
    }

    @Test
    void phoneExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888asdasd",
                "test",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Only Bulgarian phone numbers are allowed (Must include +359 at the start)!\n",exception.getMessage());
    }

    @Test
    void usernameExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test no",
                "TestTest!123",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The username cannot include special characters and whitespaces!\n",exception.getMessage());
    }

    @Test
    void incorrectPasswordExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test",
                "Test",
                "TestTest!123");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The password does not match all the rules!\n",exception.getMessage());
    }

    @Test
    void passwordsNotMatchingExceptionThrown(){
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!122");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The passwords are not the same!\n",exception.getMessage());
    }

    @Test
    void multipleInvalidFieldsExceptionThrown(){
        List<String> errorList;
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("123",
                "Test",
                "-14",
                "+359888263282",
                "test no",
                "TestTest!123",
                "TestTest!122");

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        errorList =  Arrays.stream(exception.getMessage().split("\n")).toList();
        assertEquals(4,errorList.size());
    }

    @Test
    void testValidData(){
        List<String> errorList;
        IUserFormChain verifyData = new VerifyDataChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

        assertDoesNotThrow(() -> verifyData.execute(formDTO));

    }

    @Test
    void UserAlreadyExistsException()
    {
        IUserFormChain createUserChain = new CreateUserChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "admin",
                "TestTest!123",
                "TestTest!123");

        UserAlreadyExistException exception = assertThrowsExactly(UserAlreadyExistException.class,() -> createUserChain.execute(formDTO));
        assertEquals("- A user with this username already exists!",exception.getMessage());
    }

    @Test
    void testCreateUser()
    {
        int before = userRepository.findAll().size();
        IUserFormChain createUserChain = new CreateUserChain();
        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test",
                "TestTest!123",
                "TestTest!123");

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

        UserFormDTO formDTO = new UserFormDTO("Test",
                "Test",
                "14",
                "+359888263282",
                "test1",
                "TestTest!123",
                "TestTest!123");

        assertDoesNotThrow(() -> verifyDataChain.execute(formDTO));

        int afterUser = userRepository.findAll().size();
        int afterForm = formRepository.findAll().size();

        assertTrue(afterUser>beforeUser);
        assertTrue(afterForm>beforeForm);
    }
}
