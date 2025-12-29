package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import java.awt.*;

public class LoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;


    @FXML
    public void onLogin()
    {
        String username = usernameField.getText();
        String password = passwordField.getText();
    }
}
