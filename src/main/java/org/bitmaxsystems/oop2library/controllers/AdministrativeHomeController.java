package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class AdministrativeHomeController extends BaseHomeController {
    @FXML
    private Button viewAdminButton;
    @FXML
    private Button viewLibrarianButton;
    private static final Logger logger = LogManager.getLogger(AdministrativeHomeController.class);


    @Override
    protected void initialize()
    {
        super.initialize();
        if (manager.getLoggedUser().getRole() == UserRole.LIBRARIAN)
        {
            viewAdminButton.setVisible(false);
            viewLibrarianButton.setVisible(false);
        }
    }

    private void loadAdministrativeManagementDialog(UserRole role)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.ADMINISTRATIVE_MANAGEMENT_VIEW.getPath()));
            AnchorPane root = loader.load();

            AdministrativeManagementController controller = loader.getController();
            controller.setRole(role);

            Stage stage = new Stage();
            stage.setTitle(View.ADMINISTRATIVE_MANAGEMENT_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }

    private void loadManagementDialog(View view)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(view.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }

    @FXML
    public void onViewAdmin()
    {
        loadAdministrativeManagementDialog(UserRole.ADMINISTRATOR);
    }

    @FXML
    public void onViewLibrarian()
    {
        loadAdministrativeManagementDialog(UserRole.LIBRARIAN);
    }

    @FXML
    public void onViewUserForm()
    {
        loadManagementDialog(View.USER_FORM_MANAGEMENT_VIEW);
    }

    @FXML
    public void onViewInventory()
    {
        SceneManager.showView(View.ADMINISTRATIVE_INVENTORY_VIEW);
    }
}
