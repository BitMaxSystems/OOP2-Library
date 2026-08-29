package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.dto.NotificationDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.services.NotificationService;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class BaseHomeController {

    @FXML
    private Button userDetailsButton;

    @FXML
    protected ListView<NotificationDTO> notificationListView;

    private static final Logger logger = LogManager.getLogger(BaseHomeController.class);

    protected UserManager manager = UserManager.getInstance();

    protected final NotificationService notificationService = new NotificationService();

    @FXML
    protected void initialize() {
        refreshUserDataButton();
        configureNotificationList();
        refreshNotifications();
    }

    private void refreshUserDataButton() {
        User user = manager.getLoggedUser();
        userDetailsButton.setText(user.getFirstName() + " " + user.getLastName());
    }

    private void configureNotificationList() {
        notificationListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(NotificationDTO notification, boolean empty) {
                super.updateItem(notification, empty);

                if (empty || notification == null) {
                    setText(null);
                    return;
                }

                String timestamp = notification.getTimestamp()
                        .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

                setText(
                        notification.getTitle()
                                + "\n"
                                + notification.getMessage()
                                + "\n"
                                + timestamp
                );
            }
        });
    }

    protected void refreshNotifications() {
        User user = manager.getLoggedUser();

        try {
            notificationListView.getItems().setAll(
                    notificationService.getUserNotifications(user)
            );
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Unexpected error when refreshing notifications!").show();
        }
    }

    @FXML
    public void onUserDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(View.USER_DETAILS.getPath())
            );

            AnchorPane root = loader.load();

            UserDetailsController controller = loader.getController();
            controller.setUser(UserManager.getInstance().getLoggedUser());

            Stage stage = new Stage();
            stage.setTitle(View.USER_DETAILS.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

            refreshUserDataButton();

        } catch (IOException e) {
            logger.error(e);

            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error, try again!"
            ).show();
        }
    }

    @FXML
    private void onLogout() {
        UserManager.getInstance().logoff();
        SceneManager.showView(View.LOGIN);
    }
}