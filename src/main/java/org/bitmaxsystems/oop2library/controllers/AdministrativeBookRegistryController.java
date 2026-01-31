package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.util.factory.AuthorFactory;
import org.bitmaxsystems.oop2library.util.factory.GenreFactory;
import org.bitmaxsystems.oop2library.util.factory.PublisherFactory;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;
import org.bitmaxsystems.oop2library.models.books.Genre;

import java.io.IOException;

public class AdministrativeBookRegistryController {
    private static final Logger logger = LogManager.getLogger(AdministrativeBookRegistryController.class);
    @FXML
    public void onBackToPreviousDialog()
    {
        SceneManager.showView(View.ADMINISTRATIVE_INVENTORY_VIEW);
    }

    private <T> void loadParameterManagementView(Class<T> type, String parameter, BookParameterAbstractFactory factory)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.BOOK_PARAMETER_MANAGEMENT_VIEW.getPath()));
            AnchorPane root = loader.load();

            BookParameterManagementController<T> controller = loader.getController();
            controller.setBookParameter(new BookParameterTypeDTO<>(type,factory));

            Stage stage = new Stage();
            stage.setTitle(parameter+" management");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }

    @FXML
    public void onViewGenre()
    {
        loadParameterManagementView(Genre.class,"Genre", new GenreFactory());
    }

    @FXML
    public void onViewAuthors()
    {
        loadParameterManagementView(Author.class,"Author",new AuthorFactory());
    }

    @FXML
    public void  onViewPublishers()
    {
        loadParameterManagementView(Publisher.class,"Publisher",new PublisherFactory());
    }
}
