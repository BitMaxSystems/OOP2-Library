package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.form.UserFormDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdatePasswordChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;

import java.util.UUID;

public class BasicUserDetailsController {
    @FXML
    private Label userRoleLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField loyaltyPointsField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;

    private static final Logger logger = LogManager.getLogger(BasicUserDetailsController.class);
    private GenericRepository<User> userRepository = new GenericRepository<>(User.class);

    private User user;

    public void setUser (User user)
    {
        this.user = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        if (user.getAge() == 0)
        {
            ageField.setPromptText("Age not set");
        }
        else
        {
            ageField.setText(String.valueOf(user.getAge()));
        }

        phoneField.setText(user.getPhone());
        loyaltyPointsField.setText(String.valueOf(user.getLoyaltyPoints()));

        if (user.getRole() != UserRole.ADMINISTRATOR)
        {
            loyaltyPointsField.setDisable(true);
        }

        userRoleLabel.setText(user.getRole().toString());

    }

    @FXML
    public void onUpdate()
    {
        String password, repeatPassword;

        IUserFormChain verifyUser = new VerifyDataChain();
        IUserFormChain updateUser = new UpdateUserChain();
        IUserFormChain updatePassword = new UpdatePasswordChain();

        verifyUser.setNextChain(updateUser);
        updateUser.setNextChain(updatePassword);

        UserFormDTO.Builder formDTOBuilder = new UserFormDTO.Builder(firstNameField.getText().strip(),
                lastNameField.getText().strip(),
                phoneField.getText().strip(),
                user.getCredentials().getUsername())
                .setLoyaltyPoints(loyaltyPointsField.getText().strip())
                .setAge(ageField.getText().strip())
                .setUser(user);

        if (!passwordField.getText().isBlank())
        {
            password = passwordField.getText().strip();
            repeatPassword = repeatPasswordField.getText().strip();
            formDTOBuilder = formDTOBuilder.setNewPassword(password,repeatPassword);
        }

        try {
            verifyUser.execute(formDTOBuilder.build());
            new Alert(Alert.AlertType.INFORMATION,"User Data is updated").show();
            logger.info("User data successfully submitted!");

            UserManager.getInstance().setLoggedUser(userRepository.findById(user.getId()));

        }
        catch (DataValidationException e)
        {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (NumberFormatException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        catch (Exception e)
        {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
        }

    }

    @FXML
    public void onClose()
    {
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }

}
