package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;

public class BaseUserFormController {
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

    protected void setErrors(String errors)
    {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(errors);

        passwordField.clear();
        repeatPasswordField.clear();
    }

    protected UserDataDTO.Builder generateDTO()
    {
        return new UserDataDTO.Builder(firstNameField.getText().strip(),
                lastNameField.getText().strip(),
                ageField.getText().strip(),
                phoneField.getText().strip(),
                usernameField.getText().strip())
                .setNewPassword(passwordField.getText().strip(),repeatPasswordField.getText().strip());
    }

    protected IUserFormChain setUpChain()
    {
        IUserFormChain verifyData = new VerifyDataChain();
        IUserFormChain createUser = new CreateUserChain();

        verifyData.setNextChain(createUser);

        return verifyData;
    }


    @FXML
    public void onSubmit() throws Exception {
        resetErrorLabel();

        IUserFormChain userCreationChain = setUpChain();

        UserDataDTO formData = generateDTO()
                .build();


        userCreationChain.execute(formData);
    }
}
