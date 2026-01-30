package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.UserRepository;

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
    public void initialize()
    {
        firstNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("LastName"));
        phoneColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("phone"));
        ageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        loyaltyPointsColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("loyaltyPoints"));
        dateOfApprovalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateOfApproval"));
        userRoleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));
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
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again");
        }
    }
}
