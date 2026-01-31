package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.form.enums.FormStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class UserFormManagementController {
    @FXML
    private Label pendingStatusLabel;
    @FXML
    private TableView<UserForm> tableView;
    @FXML
    private TableColumn<UserForm, Date> submissionDateColumn;
    @FXML
    private TableColumn<UserForm, FormStatus> formStatusColumn;
    @FXML
    private TableColumn<UserForm,User> userColumn;
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
    private TableColumn<UserForm,Date> dateOfApprovalColumn;
    @FXML
    private TableColumn<UserForm, UserRole> userRoleColumn;

    private GenericRepository<UserForm> userFormGenericRepository = new GenericRepository<>(UserForm.class);

    private static final Logger logger = LogManager.getLogger(UserFormManagementController.class);

    @FXML
    private void initialize()
    {
        submissionDateColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("dateOfCreation"));
        formStatusColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("status"));
        userColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("user"));


        firstNameColumn.setCellValueFactory(
                userForm -> new SimpleStringProperty(userForm.getValue().getUser().getFirstName()));
        lastNameColumn.setCellValueFactory(
                userForm -> new SimpleStringProperty(userForm.getValue().getUser().getLastName()));
        phoneColumn.setCellValueFactory(
                userForm -> new SimpleStringProperty(userForm.getValue().getUser().getPhone()));
        ageColumn.setCellValueFactory(
                userForm -> new SimpleObjectProperty<>(userForm.getValue().getUser().getAge()));
        loyaltyPointsColumn.setCellValueFactory(
                userForm -> new SimpleObjectProperty<>(userForm.getValue().getUser().getLoyaltyPoints()));
        dateOfApprovalColumn.setCellValueFactory(
                userForm -> new SimpleObjectProperty<>(userForm.getValue().getUser().getDateOfApproval()));
        userRoleColumn.setCellValueFactory(
                userForm -> new SimpleObjectProperty<>(userForm.getValue().getUser().getRole()));;

        userColumn
                .getColumns()
                .setAll(firstNameColumn,
                        lastNameColumn,
                        phoneColumn,
                        ageColumn,
                        loyaltyPointsColumn,
                        dateOfApprovalColumn,
                        userRoleColumn);

        tableView.setOnMouseClicked(this::onDoubleClick);
        refreshTable();
    }

    private void loadUserFormDetails(UserForm form)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.USER_FORM_DETAILS_VIEW.getPath()));
            AnchorPane root = loader.load();

            UserFromDetailsController controller = loader.getController();
            controller.setUserForm(form);

            Stage stage = new Stage();
            stage.setTitle(View.USER_FORM_DETAILS_VIEW.getTitle());
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
            UserForm selectedUserForm = tableView.getSelectionModel().getSelectedItem();
            if (selectedUserForm != null)
            {
                loadUserFormDetails(selectedUserForm);
                refreshTable();
            }
        }
    }

    private void refreshTable()
    {
        try {
            List<UserForm> userFormsList = userFormGenericRepository.findAll();
            int pendingCount = Math.toIntExact(userFormsList.
                    stream()
                    .filter(uf -> uf.getStatus() == FormStatus.PENDING).count());

            if (pendingCount > 0) {
                pendingStatusLabel.setText("Pending forms: " + pendingCount);
            } else
            {
                pendingStatusLabel.setText("No pending forms");
            }
            tableView.getItems().setAll(userFormsList);
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }
}
