package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class AdministrativeDialogController extends BaseDialogController {
    private static final Logger logger = LogManager.getLogger(AdministrativeDialogController.class);

    @FXML
    public void onViewAdmin()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.ADMINISTRATIVE_MANAGEMENT_VIEW.getPath()));
            AnchorPane root = loader.load();

            AdministrativeManagementController controller = loader.getController();
            controller.setRole(UserRole.ADMINISTRATOR);

            Stage stage = new Stage();
            stage.setTitle(View.ADMINISTRATIVE_MANAGEMENT_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!");
        }
    }
}
