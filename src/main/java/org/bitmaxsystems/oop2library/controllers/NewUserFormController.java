package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.FormDataValidationException;
import org.bitmaxsystems.oop2library.exceptions.UserAlreadyExistException;
import org.bitmaxsystems.oop2library.models.form.UserFormDTO;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.SaveFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

public class NewUserFormController {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;

    private static final Logger logger = LogManager.getLogger(NewUserFormController.class);

    private void resetErrorLabel()
    {
        String string = """
                - Password must contain 1 number (0-9)
                - Password must contain 1 uppercase letters
                - Password must contain 1 lowercase letters
                - Password must contain 1 non-alpha numeric number
                - Password is 8-16 characters with no space
                """;
        errorLabel.setTextFill(Color.BLACK);
        errorLabel.setText(string);
    }

    @FXML
    public void onLoginRedirect()
    {
        SceneManager.showView(View.LOGIN);
    }
    
    @FXML
    public void onSubmit()
    {
        IUserFormChain verifyData = new VerifyDataChain();
        IUserFormChain createUser = new CreateUserChain();
        IUserFormChain saveForm = new SaveFormChain();

        resetErrorLabel();

        verifyData.setNextChain(createUser);
        createUser.setNextChain(saveForm);

        UserFormDTO formData = new UserFormDTO(
                firstNameField.getText(),
                lastNameField.getText(),
                ageField.getText(),
                phoneField.getText(),
                usernameField.getText(),
                passwordField.getText(),
                repeatPasswordField.getText()
        );

        try{
            verifyData.execute(formData);

            new Alert(Alert.AlertType.INFORMATION,"Form is submitted!").show();
            logger.info("Form successfully submitted!");
            SceneManager.showView(View.LOGIN);
        }
        catch (FormDataValidationException e)
        {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR,"Invalid data found").show();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText(e.getMessage());
        }
        catch (UserAlreadyExistException e)
        {
            logger.error("User with this username already exists");
            new Alert(Alert.AlertType.ERROR,"User with this username already exists").show();
            errorLabel.setTextFill(Color.RED);
            errorLabel.setText(e.getMessage());
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
        }


    }
}
