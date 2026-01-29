package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        User user = UserManager.getInstance().getLoggedUser();
        welcomeText.setText("Welcome " +user.getFirstName()+" "+user.getLastName()+" to Library Application!");
    }

    private static final Logger logger = LogManager.getLogger(HelloController.class);

    @FXML
    protected void onLogout() {

        logger.info("User logged out");
        UserManager.getInstance().logoff();
        SceneManager.showView(View.LOGIN);
    }
}