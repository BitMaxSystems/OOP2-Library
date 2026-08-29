package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.util.chain.userform.SaveFormChain;
import org.bitmaxsystems.oop2library.util.chain.userform.SendFormNotificationChain;
import org.bitmaxsystems.oop2library.util.chain.userform.contract.IUserFormChain;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

public class UserFormController extends BaseUserFormController{
    private static final Logger logger = LogManager.getLogger(UserFormController.class);

    @FXML
    public void onLoginRedirect()
    {
        SceneManager.showView(View.LOGIN);
    }

    @Override
    protected void setUpChain() {

        IUserFormChain saveForm = new SaveFormChain();
        IUserFormChain sendNotification = new SendFormNotificationChain();

        saveForm.setNextChain(sendNotification);
        userService.setUpChain(saveForm);

    }

    @FXML
    public void onSubmit() {
        if (submitForm())
        {
            new Alert(Alert.AlertType.INFORMATION, "Form is submitted!").show();
            logger.info("Form successfully submitted!");
            SceneManager.showView(View.LOGIN);
        }
    }
}
