package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
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
            UserManager manager = UserManager.getInstance();
            manager.login(username,password);
            new Alert(Alert.AlertType.INFORMATION, "Hello, " + username).show();
            UserRole role = manager.getLoggedUser().getRole();
            if (role == UserRole.READER)
            {
                SceneManager.showView(View.BASE_HOME_VIEW);
            }
            else if (role == UserRole.ADMINISTRATOR || role == UserRole.LIBRARIAN)
            {
                SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
            }
            else
            {
                SceneManager.showView(View.BASE_HOME_VIEW);
            }

        }
        catch (DataValidationException e)
        {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
            throw new RuntimeException(e);
        }
        finally {
            usernameField.clear();
            passwordField.clear();
        }
    }

    @FXML
    public void onNewUserRedirect()
    {
        SceneManager.showView(View.NEW_USER_FORM);
    }
}
