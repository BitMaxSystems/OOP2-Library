package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.util.contracts.IUserFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.CreateUserChain;
import org.bitmaxsystems.oop2library.util.userformchain.SaveFormChain;
import org.bitmaxsystems.oop2library.util.userformchain.VerifyDataChain;
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
    protected IUserFormChain setUpChain() {

        IUserFormChain verifyData = new VerifyDataChain();
        IUserFormChain createUser = new CreateUserChain();
        IUserFormChain saveForm = new SaveFormChain();

        verifyData.setNextChain(createUser);
        createUser.setNextChain(saveForm);

        return verifyData;
    }

    @Override
    public void onSubmit() {
        try
        {
            super.onSubmit();
            new Alert(Alert.AlertType.INFORMATION, "Form is submitted!").show();
            logger.info("Form successfully submitted!");
            SceneManager.showView(View.LOGIN);
        }
        catch (DataValidationException e) {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            super.setErrors(e.getMessage());

        } catch (DataAlreadyExistException e) {
            logger.error("User with this username already exists");
            new Alert(Alert.AlertType.ERROR, "User with this username already exists").show();
            super.setErrors(e.getMessage());
        } catch (NumberFormatException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }
    }
}
