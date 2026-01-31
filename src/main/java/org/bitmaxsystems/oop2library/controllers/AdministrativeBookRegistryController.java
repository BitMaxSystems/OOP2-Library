package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.factory.AuthorFactory;
import org.bitmaxsystems.oop2library.util.factory.GenreFactory;
import org.bitmaxsystems.oop2library.util.factory.PublisherFactory;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;
import org.bitmaxsystems.oop2library.models.books.Genre;

import java.io.IOException;

public class AdministrativeBookRegistryController {
    @FXML
    private TableView<Book> tableView;
    @FXML
    public TableColumn<Book,String> isbnColumn;
    @FXML
    public TableColumn<Book,String> titleColumn;
    @FXML
    public TableColumn<Book,String> authorColumn;
    @FXML
    public TableColumn<Book,String> genreColumn;
    @FXML
    public TableColumn<Book,String> publisherColumn;

    private static final Logger logger = LogManager.getLogger(AdministrativeBookRegistryController.class);
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);


    @FXML
    private void initialize()
    {
        isbnColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("isbn"));
        titleColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("author"));
        genreColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("genre"));
        publisherColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("publisher"));

       // tableView.setOnMouseClicked(this::onDoubleClick);
        refreshTable();
    }

    private void refreshTable()
    {
        try
        {
            tableView.getItems().setAll(bookGenericRepository.findAll());
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

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

    @FXML
    public void onCreate()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.BOOK_REGISTRY_CREATION_VIEW.getPath()));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(View.BOOK_REGISTRY_CREATION_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }
}
