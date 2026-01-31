package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.FormAlreadyApprovedException;
import org.bitmaxsystems.oop2library.models.form.UserForm;
import org.bitmaxsystems.oop2library.models.form.enums.FormStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.util.ApproveUserFormService;

public class UserFromDetailsController {
    @FXML
    private Label headerLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label ageLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label loyaltyPointsLabel;
    @FXML
    private Label userRoleLabel;
    @FXML
    private Label dateOfApprovalLabel;
    @FXML
    private Label submissionDateLabel;
    @FXML
    private Label formStatusLabel;
    @FXML
    private Button approvalButton;
    private UserForm form;
    private static final Logger logger = LogManager.getLogger(UserFromDetailsController.class);

    public void setUserForm(UserForm form)
    {
        this.form = form;
        resetControls();

    }

    private void resetControls() {
        User user = this.form.getUser();
        headerLabel.setText("User Form: " + user.getFirstName() + " " + user.getLastName());
        firstNameLabel.setText(user.getFirstName());
        lastNameLabel.setText(user.getLastName());
        ageLabel.setText(String.valueOf(user.getAge()));
        phoneLabel.setText(user.getPhone());
        loyaltyPointsLabel.setText(String.valueOf(user.getLoyaltyPoints()));
        userRoleLabel.setText(user.getRole().toString());

        if (user.getDateOfApproval() == null)
        {
            dateOfApprovalLabel.setText("Awaiting approval");
        }
        else
        {
            dateOfApprovalLabel.setText(String.valueOf(user.getDateOfApproval()));
        }
        submissionDateLabel.setText(String.valueOf(form.getDateOfCreation()));
        formStatusLabel.setText(form.getStatus().toString());

        if (form.getStatus() == FormStatus.APPROVED)
        {
            approvalButton.setVisible(false);
        }
    }

    @FXML
    public void onApprove()
    {
        ApproveUserFormService approveUserFormService = new ApproveUserFormService();

        try {
            approveUserFormService.approveUser(form);
            User user = form.getUser();
            resetControls();
            logger.info("{} {} successfully approved", user.getFirstName(), user.getLastName());
            new Alert(Alert.AlertType.INFORMATION,user.getFirstName() +" "+user.getLastName()+" successfully approved")
                    .show();
        } catch (FormAlreadyApprovedException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again.").show();
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) headerLabel.getScene().getWindow()).close();
    }
}
