package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.models.auth.Credentials;
import org.bitmaxsystems.oop2library.repository.AuthorisationRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    private static final Logger logger = LogManager.getLogger(LoginController.class);
    private final AuthorisationRepository authorisationRepository = new AuthorisationRepository();

    @FXML
    public void onLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        Credentials userCredentials = authorisationRepository.getUserAuthorisation(username);

        if (userCredentials == null) {
            logger.error("{} not found in the database", username);
            new Alert(Alert.AlertType.ERROR, "Invalid credentials").show();
        } else {
            if (userCredentials.equals(username, password)) {
                logger.info("{} logged successfully",username);
                new Alert(Alert.AlertType.INFORMATION, "Hello, " + username).show();
                UserManager.getInstance().login();
                SceneManager.showView(View.MAIN_VIEW);
            } else {
                logger.error("{} inputted wrong password", username);
                new Alert(Alert.AlertType.ERROR, "Invalid credentials").show();
            }
        }
    }
}
