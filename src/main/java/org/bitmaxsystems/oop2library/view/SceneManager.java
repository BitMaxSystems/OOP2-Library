package org.bitmaxsystems.oop2library.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class SceneManager {
    private static Stage primaryStage;
    private static final Logger logger = LogManager.getLogger(SceneManager.class);

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void showView(View view) {
        try{
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(view.getPath()));
            Scene scene = new Scene(loader.load(), view.getWidth(), view.getHeight());
            primaryStage.setScene(scene);
            primaryStage.setTitle(view.getTitle());
            primaryStage.show();
        }
        catch (IOException e)
        {
            logger.error(e);
            throw new RuntimeException(e);
        }
    }
}
