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
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.services.UserService;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReaderManagementController {

    @FXML
    private TableView<User> tableView;
    @FXML
    private TableColumn<UserForm,String> firstNameColumn;
    @FXML
    private TableColumn<UserForm,String> lastNameColumn;
    @FXML
    private TableColumn<UserForm,Integer> ageColumn;
    @FXML
    private TableColumn<UserForm,String> phoneColumn;
    @FXML
    private TableColumn<UserForm,Integer> loyaltyPointsColumn;
    @FXML
    private TableColumn<UserForm,LocalDate> dateOfApprovalColumn;
    @FXML
    private TableColumn<UserForm, UserRole> userRoleColumn;

    private UserService userService = new UserService();

    private static final Logger logger = LogManager.getLogger(ReaderManagementController.class);

    @FXML
    private void initialize()
    {

        firstNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("lastName"));
        phoneColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("phone"));
        ageColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("age"));
        loyaltyPointsColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("loyaltyPoints"));
        dateOfApprovalColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateOfApproval"));
        userRoleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("role"));

        tableView.setOnMouseClicked(this::onDoubleClick);
        refreshTable();
    }

    private void loadUserDetails(User user)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.READER_DETAILS_VIEW.getPath()));
            AnchorPane root = loader.load();

            ReaderDetailsController controller = loader.getController();
            controller.setUser(user);

            Stage stage = new Stage();
            stage.setTitle(View.READER_DETAILS_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!");
        }
    }


    private void onDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2)
        {
            User selectedUser = tableView.getSelectionModel().getSelectedItem();
            if (selectedUser != null)
            {
                loadUserDetails(selectedUser);
                refreshTable();
            }
        }
    }

    private void refreshTable()
    {
        try {
            List<UserRole> userRoleList = new ArrayList<>();
            userRoleList.add(UserRole.READER);
            userRoleList.add(UserRole.UNAPPROVED_READER);
            List<User> userList = userService.getUsersByRoles(userRoleList);
            tableView.getItems().setAll(userList);
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onBack()
    {
        SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
    }
}
