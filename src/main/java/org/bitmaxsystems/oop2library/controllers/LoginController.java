package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.repository.AuthorisationRepository;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private final AuthorisationRepository authorisationRepository = new AuthorisationRepository();

    @FXML
    public void onLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Credentials userCredentials = authorisationRepository.getUserAuthorisation(username);

        if (userCredentials == null)
        {
            new Alert(Alert.AlertType.ERROR, "Invalid credentials").show();
        }
        else
        {
            if (userCredentials.equals(username,password))
            {
                new Alert(Alert.AlertType.INFORMATION,"Hello, "+username).show();
            }
            else
            {
                new Alert(Alert.AlertType.ERROR,"Invalid credentials").show();
            }
        }
    }
}
