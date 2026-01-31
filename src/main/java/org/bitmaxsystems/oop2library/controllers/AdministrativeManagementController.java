package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.View;

import java.util.Date;

public class AdministrativeManagementController {
    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> phoneColumn;
    @FXML
    private TableColumn<User, Integer> ageColumn;
    @FXML
    private TableColumn<User, Integer> loyaltyPointsColumn;
    @FXML
    private TableColumn<User, Date> dateOfApprovalColumn;
    @FXML
    private TableColumn<User,UserRole> userRoleColumn;
    private UserRepository userRepository = new UserRepository();
    private UserRole role;

    private static final Logger logger = LogManager.getLogger(AdministrativeManagementController.class);


    @FXML
    private void initialize()
    {
        firstNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("LastName"));
        phoneColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("phone"));
        ageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        loyaltyPointsColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("loyaltyPoints"));
        dateOfApprovalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateOfApproval"));
        userRoleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));

        tableView.setOnMouseClicked(this::onTableClick);
    }

    @FXML
    public void onCreate()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.NEW_ADMINISTRATION_USER_FORM.getPath()));
            AnchorPane root = loader.load();

            AdministrativeFormController controller = loader.getController();
            controller.setRole(role);

            Stage stage = new Stage();
            stage.setTitle(View.NEW_ADMINISTRATION_USER_FORM.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }

    }

    private void onTableClick(MouseEvent event)
    {
        if (event.getClickCount() == 2)
        {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null)
            {
                loadUserDetails(selectedUser);
            }
        }
    }

    private void loadUserDetails(User user)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.BASIC_USER_DETAILS.getPath()));
            AnchorPane root = loader.load();

            BasicUserDetailsController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(View.BASIC_USER_DETAILS.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    public void setRole(UserRole role)
    {
        this.role = role;
        refreshTable();
    }

    private void refreshTable()
    {
        try
        {
            tableView.getItems().setAll(userRepository.searchByRole(role));
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }
}
