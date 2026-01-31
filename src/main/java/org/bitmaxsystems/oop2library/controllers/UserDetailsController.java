package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;

public class UserDetailsController extends BasicUserDetailsController{
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField repeatPasswordField;

    @Override
    protected UserDataDTO.Builder generateDTO() {
        String password, repeatPassword;
        UserDataDTO.Builder formDTOBuilder =  super.generateDTO();

        if (!passwordField.getText().isBlank())
        {
            password = passwordField.getText().strip();
            repeatPassword = repeatPasswordField.getText().strip();
            formDTOBuilder = formDTOBuilder.setNewPassword(password,repeatPassword);
        }

        return formDTOBuilder;
    }

    @Override
    protected void onUpdate() {
        super.onUpdate();
        passwordField.clear();
        repeatPasswordField.clear();
    }
}

