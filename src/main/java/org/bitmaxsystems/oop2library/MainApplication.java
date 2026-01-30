package org.bitmaxsystems.oop2library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class MainApplication extends Application {
    private static final Logger logger = LogManager.getLogger(MainApplication.class);

    @Override
    public void start(Stage stage) throws IOException {
        try
        {
            HibernateInit.initializeIfEmpty();
            SceneManager.setPrimaryStage(stage);
            SceneManager.showView(View.LOGIN);
        } catch (Exception e) {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}