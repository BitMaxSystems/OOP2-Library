package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.UserDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdatePasswordChain;
import org.bitmaxsystems.oop2library.util.userformchain.UpdateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;

public class BasicUserDetailsController {
    @FXML
    private Label userRoleLabel;
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

    private User user;

    public void setUser(User user) {
        this.user = user;
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        ageField.setText(String.valueOf(user.getAge()));
        phoneField.setText(user.getPhone());
        loyaltyPointsField.setText(String.valueOf(user.getLoyaltyPoints()));
        userRoleLabel.setText(user.getRole().toString());

        if (user.getDateOfApproval() == null) {
            dateOfApprovalLabel.setText("Awaiting approval");
        } else {
            dateOfApprovalLabel.setText(String.valueOf(user.getDateOfApproval()));
        }

        if (user.getRole() != UserRole.ADMINISTRATOR) {
            loyaltyPointsField.setDisable(true);
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
                .setUser(user);
    }

    @FXML
    public void onUpdate()
    {
        IUserFormChain verifyUser = new VerifyDataChain();
        IUserFormChain updateUser = new UpdateUserChain();
        IUserFormChain updatePassword = new UpdatePasswordChain();

        verifyUser.setNextChain(updateUser);
        updateUser.setNextChain(updatePassword);

        UserDataDTO.Builder formDTOBuilder = generateDTO();

        try {
            verifyUser.execute(formDTOBuilder.build());
            new Alert(Alert.AlertType.INFORMATION,"User Data is updated").show();
            logger.info("User data successfully submitted!");

        }
        catch (DataValidationException e)
        {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (NumberFormatException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        catch (Exception e)
        {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred, try again.").show();
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) userRoleLabel.getScene().getWindow()).close();
    }
}
