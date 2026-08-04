import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyUserDataChain;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserDataValidationTest {


    @Test
    void firstNameExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("213",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();


        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- First name can only be word characters without whitespaces!\n",exception.getMessage());
    }

    @Test
    void lastNameExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO =  new UserDataDTO.Builder("Test",
                "123",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Last name can only be word characters without whitespaces!\n",exception.getMessage());
    }


    @Test
    void ageNonNumericExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "das",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Age must be a numeric value!\n",exception.getMessage());
    }

    @Test
    void ageOutOfIntervalExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "-2",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Age must be between 13 and 100!\n",exception.getMessage());
    }

    @Test
    void loyaltyPointsNonNumericExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setLoyaltyPoints("das")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Loyalty points must be a numeric value!\n",exception.getMessage());
    }

    @Test
    void loyaltyPointsOutOfIntervalExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setLoyaltyPoints("-2")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Loyalty points must be between 0 and 100!\n",exception.getMessage());
    }

    @Test
    void phoneExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+35988826asdasd",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- Only Bulgarian phone numbers are allowed (Must include +359 at the start)!\n",exception.getMessage());
    }

    @Test
    void usernameExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test no")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The username cannot include special characters and whitespaces!\n",exception.getMessage());
    }

    @Test
    void incorrectPasswordExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("Test","TestTest!123")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The password does not match all the rules!\n",exception.getMessage());
    }

    @Test
    void passwordsNotMatchingExceptionThrown(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!122")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        assertEquals("- The passwords are not the same!\n",exception.getMessage());
    }

    @Test
    void multipleInvalidFieldsExceptionThrown(){
        List<String> errorList;
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("123",
                "Test",
                "-14",
                "+359888263282",
                "test no")
                .setNewPassword("TestTest!123","TestTest!122")
                .build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,() -> verifyData.execute(formDTO));
        errorList =  Arrays.stream(exception.getMessage().split("\n")).toList();
        assertEquals(4,errorList.size());
    }

    @Test
    void testValidDataWithoutLoyaltyPoints(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> verifyData.execute(formDTO));

    }

    @Test
    void testValidDataWithLoyaltyPoints(){
        IUserFormChain verifyData = new VerifyUserDataChain();
        UserDataDTO formDTO = new UserDataDTO.Builder("Test",
                "Test",
                "14",
                "+359888263282",
                "test")
                .setLoyaltyPoints("74")
                .setNewPassword("TestTest!123","TestTest!123")
                .build();

        assertDoesNotThrow(() -> verifyData.execute(formDTO));

    }
}
