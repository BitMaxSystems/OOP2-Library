package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
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


    @FXML
    public void onLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            UserManager.getInstance().login(username,password);
            new Alert(Alert.AlertType.INFORMATION, "Hello, " + username).show();
            SceneManager.showView(View.MAIN_VIEW);

        }
        catch (DataValidationException e)
        {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
    }

    @FXML
    public void onNewUserRedirect()
    {
        SceneManager.showView(View.NEW_USER_FORM);
    }
}
