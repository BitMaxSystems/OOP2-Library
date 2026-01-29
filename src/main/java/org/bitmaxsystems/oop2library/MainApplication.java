package org.bitmaxsystems.oop2library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        HibernateInit.initializeIfEmpty();
        SceneManager.setPrimaryStage(stage);
        SceneManager.showView(View.LOGIN);
    }

    public static void main(String[] args) {
        launch();
    }
}