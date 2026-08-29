package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;

public class AdministrativeFormController extends BaseUserFormController{
    @FXML
    private Label headerLabel;

    private static final Logger logger = LogManager.getLogger(AdministrativeFormController.class);
    private UserRole role;

    public void setRole(UserRole role) {
        this.role = role;
        headerLabel.setText("New "+role.toString());
    }

    @Override
    protected UserDataDTO.Builder generateDTO() {
        return super.generateDTO().setRole(role);
    }

    @FXML
    public void onSubmit() {
        if (submitForm())
        {
            new Alert(Alert.AlertType.INFORMATION, role.toString()+" is successfully created!").show();
            logger.info("{} successfully created!", role.toString());
            ((Stage) headerLabel.getScene().getWindow()).close();
        }
    }
}
