package org.bitmaxsystems.oop2library.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.services.UserService;
import org.bitmaxsystems.oop2library.util.UserManager;

import java.util.Optional;

public class BasicUserDetailsController {
    @FXML
    private ChoiceBox<UserRole> userRoleChoiceBox;
    @FXML
    private Label dateOfApprovalLabel;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField loyaltyPointsField;
    private static final Logger logger = LogManager.getLogger(BasicUserDetailsController.class);
    private UserManager manager = UserManager.getInstance();
    protected User user;
    private UserService userService = new UserService();

    @FXML
    protected void initialize()
    {
        userRoleChoiceBox.setItems(FXCollections.observableArrayList(UserRole.values()));
    }

    public void setUser(User user) {
        this.user = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        ageField.setText(String.valueOf(user.getAge()));
        phoneField.setText(user.getPhone());
        loyaltyPointsField.setText(String.valueOf(user.getLoyaltyPoints()));
        userRoleChoiceBox.setValue(user.getRole());

        if (user.getDateOfApproval() == null) {
            dateOfApprovalLabel.setText("Awaiting approval");
        } else {
            dateOfApprovalLabel.setText(String.valueOf(user.getDateOfApproval()));
        }

        User currentlyLoggedUser = manager.getLoggedUser();

        if (currentlyLoggedUser.getRole() != UserRole.ADMINISTRATOR) {
            loyaltyPointsField.setDisable(true);
        }

        if (currentlyLoggedUser.equals(user) || currentlyLoggedUser.getRole() != UserRole.ADMINISTRATOR)
        {
            userRoleChoiceBox.setDisable(true);
        }
    }

    protected UserDataDTO.Builder generateDTO()
    {
        return new UserDataDTO.Builder(firstNameField.getText().strip(),
                lastNameField.getText().strip(),
                ageField.getText().strip(),
                phoneField.getText().strip(),
                user.getCredentials().getUsername())
                .setLoyaltyPoints(loyaltyPointsField.getText().strip())
                .setRole(userRoleChoiceBox.getValue())
                .setUser(user);
    }

    @FXML
    protected void onUpdate()
    {

        UserDataDTO.Builder formDTOBuilder = generateDTO();
        try {
            userService.updateUser(formDTOBuilder.build());
            new Alert(Alert.AlertType.INFORMATION,"User Data is updated").show();

        }
        catch (DataValidationException | NumberFormatException e)
        {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (Exception e)
        {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
        }
    }

    @FXML
    public void onDelete()
    {
        if (manager.getLoggedUser().equals(user))
        {
            new Alert(Alert.AlertType.ERROR,"Cannot delete the user, because you are currently logged in with this user!").show();
        }
        else
        {
            String userFullName = user.getFirstName() + " " + user.getLastName();
            Optional<ButtonType> alertResult =  new Alert(Alert.AlertType.WARNING,
                    "Are you sure you want to delete "+userFullName,
                    ButtonType.YES,
                    ButtonType.NO)
                    .showAndWait();

            if (alertResult.isPresent() && alertResult.get() == ButtonType.YES)
            {

                try
                {
                    userService.deleteUser(user);
                    new Alert(Alert.AlertType.INFORMATION,userFullName+" successfully deleted!").show();
                    onClose();
                }
                catch (ChildRecordExistException e) {
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
                catch (Exception e) {
                    new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
                }

            }
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) userRoleChoiceBox.getScene().getWindow()).close();
    }
}
