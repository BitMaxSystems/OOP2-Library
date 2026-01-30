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
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class BaseDialogController {

    @FXML
    private Button userDetailsButton;

    private static final Logger logger = LogManager.getLogger(BaseDialogController.class);

    @FXML
    public void initialize()
    {
       refreshUserDataButton();
    }

    private void refreshUserDataButton()
    {
        User user = UserManager.getInstance().getLoggedUser();
        userDetailsButton.setText(user.getFirstName()+" "+user.getLastName());
    }

    @FXML
    public void onUserDetails()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.BASIC_USER_DETAILS.getPath()));
            AnchorPane root = loader.load();

            BasicUserDetailsController controller = loader.getController();
            controller.setUser(UserManager.getInstance().getLoggedUser());

            Stage stage = new Stage();
            stage.setTitle(View.BASIC_USER_DETAILS.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshUserDataButton();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!");
        }
    }

    @FXML
    private void onLogout() {
        logger.info("User logged out");
        UserManager.getInstance().logoff();
        SceneManager.showView(View.LOGIN);
    }
}