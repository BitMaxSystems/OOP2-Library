import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.service.ApproveUserFormService;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.SaveFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyUserDataChain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserFormTest {

    private GenericRepository<User> userGenericRepository = new GenericRepository<>(User.class);
    private GenericRepository<UserForm> userFormGenericRepository = new GenericRepository<>(UserForm.class);

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

        DataAlreadyExistException exception = assertThrowsExactly(DataAlreadyExistException.class,() -> createUserChain.execute(formDTO));
        assertEquals("- A user with this username already exists!",exception.getMessage());
    }

    @Test
    void testCreateUser()
    {
        int before = userGenericRepository.findAll().size();
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> createUserChain.execute(formDTO));

        List<User> afterUsers = userGenericRepository.findAll();

        int after = afterUsers.size();
        assertTrue(after>before);


        User newUser = afterUsers.stream()
                .filter(u -> u.getLastName().equals("Test") &&  u.getFirstName().equals("Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(newUser);
        assertEquals(UserRole.UNAPPROVED_READER,newUser.getRole());
    }

    @Test
    void testCreateAdministrator()
    {
        int before = userGenericRepository.findAll().size();
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test1",
                "Test1",
                "14",
                "+359888263282",
                "test1")
                .setNewPassword("TestTest!123","TestTest!123")
                .setRole(UserRole.ADMINISTRATOR)
                .build();

        assertDoesNotThrow(() -> createUserChain.execute(formDTO));

        List<User> afterUsers = userGenericRepository.findAll();

        int after = afterUsers.size();
        assertTrue(after>before);


        User newUser = afterUsers.stream()
                .filter(u -> u.getLastName().equals("Test1") &&  u.getFirstName().equals("Test1"))
                .findFirst()
                .orElse(null);

        assertNotNull(newUser);
        assertEquals(UserRole.ADMINISTRATOR,newUser.getRole());
    }

    @Test
    void testCreateLibrarian()
    {
        int before = userGenericRepository.findAll().size();
        IUserFormChain createUserChain = new CreateUserChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test2",
                "Test2",
                "14",
                "+359888263282",
                "test2")
                .setNewPassword("TestTest!123","TestTest!123")
                .setRole(UserRole.LIBRARIAN)
                .build();

        assertDoesNotThrow(() -> createUserChain.execute(formDTO));

        List<User> afterUsers = userGenericRepository.findAll();

        int after = afterUsers.size();
        assertTrue(after>before);


        User newUser = afterUsers.stream()
                .filter(u -> u.getFirstName().equals("Test2") &&  u.getLastName().equals("Test2"))
                .findFirst()
                .orElse(null);

        assertNotNull(newUser);
        assertEquals(UserRole.LIBRARIAN,newUser.getRole());
    }

    @Test
    void testNewForm()
    {
        int beforeUser = userGenericRepository.findAll().size();
        int beforeForm = userFormGenericRepository.findAll().size();
        IUserFormChain verifyDataChain = new VerifyUserDataChain();
        IUserFormChain createUserChain = new CreateUserChain();
        IUserFormChain saveFormChain = new SaveFormChain();

        verifyDataChain.setNextChain(createUserChain);
        createUserChain.setNextChain(saveFormChain);

        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test3")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> verifyDataChain.execute(formDTO));

        int afterUser = userGenericRepository.findAll().size();
        int afterForm = userFormGenericRepository.findAll().size();

        assertTrue(afterUser>beforeUser);
        assertTrue(afterForm>beforeForm);
    }

    UserForm generateIndependentForm()
    {
        IUserFormChain verifyDataChain = new VerifyUserDataChain();
        IUserFormChain createUserChain = new CreateUserChain();
        IUserFormChain saveFormChain = new SaveFormChain();

        verifyDataChain.setNextChain(createUserChain);
        createUserChain.setNextChain(saveFormChain);

        UserDataDTO formDTO = new UserDataDTO.Builder("Form",
                "Test",
                "14",
                "+359888263282",
                "formTest")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> verifyDataChain.execute(formDTO));

        User newUser = userGenericRepository.findAll().stream()
                .filter(u -> u.getFirstName().equals("Form") &&  u.getLastName().equals("Test"))
                .findFirst()
                .orElse(null);

        assertNotNull(newUser);

        return newUser.getForm();
    }

    @Test
    void testUserApproval()
    {
        UserForm form = generateIndependentForm();

        assertNotNull(form);

        ApproveUserFormService approveUserFormService = new ApproveUserFormService();

        assertDoesNotThrow(() -> approveUserFormService.approveUser(form));

        User approvedUser = form.getUser();

        assertEquals(UserRole.READER,approvedUser.getRole());
    }
}
