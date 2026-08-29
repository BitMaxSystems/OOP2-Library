package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.services.UserFormService;

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

    protected UserFormService userService = new UserFormService();

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

    protected void setUpChain()
    {
        userService.setUpChain(null);
    }

    protected boolean submitForm()
    {
        boolean isSuccessful = false;
        try {
            resetErrorLabel();

            setUpChain();

            UserDataDTO formData = generateDTO()
                    .build();

            userService.executeChain(formData);
            isSuccessful = true;
        }
        catch (DataValidationException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            setErrors(e.getMessage());
        } catch (DataAlreadyExistException e) {
            new Alert(Alert.AlertType.ERROR, "User with this username already exists").show();
            setErrors(e.getMessage());
        } catch (NumberFormatException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }

        return isSuccessful;

    }

}
