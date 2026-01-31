package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.exceptions.UserAlreadyExistException;
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

    @Override
    public void onSubmit() {
        try
        {
            super.onSubmit();
            new Alert(Alert.AlertType.INFORMATION, "Admin is created!").show();
            logger.info("Admin successfully created!");
            ((Stage) headerLabel.getScene().getWindow()).close();
        }
        catch (DataValidationException e) {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            super.setErrors(e.getMessage());

        } catch (UserAlreadyExistException e) {
            logger.error("User with this username already exists");
            new Alert(Alert.AlertType.ERROR, "User with this username already exists").show();
            super.setErrors(e.getMessage());
        } catch (NumberFormatException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }
    }
}
