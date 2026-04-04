package org.bitmaxsystems.oop2library.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class BaseBookRegistryFormControl {
    @FXML
    private Label errorLabel;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField titleField;
    @FXML
    private ChoiceBox<Genre> genreChoiceBox;
    @FXML
    private ChoiceBox<Author> authorChoiceBox;
    @FXML
    private ChoiceBox<Publisher> publisherChoiceBox;
    private static final Logger logger = LogManager.getLogger(BaseBookRegistryFormControl.class);
    private GenericRepository<Genre> genreGenericRepository = new GenericRepository<>(Genre.class);
    private GenericRepository<Author> authorGenericRepository = new GenericRepository<>(Author.class);
    private GenericRepository<Publisher> publisherGenericRepository = new GenericRepository<>(Publisher.class);

    private void resetErrorLabel() {
        String string =
                "- ISBN is 13 digits in format: xxx-x-xx-xxxxxx-x. Example: 1234-5-67-891234-5.";
        errorLabel.setTextFill(Color.BLACK);
        errorLabel.setText(string);
    }

    @FXML
    public void initialize()
    {
        resetErrorLabel();
        genreChoiceBox.setItems(FXCollections.observableArrayList(genreGenericRepository.findAll()));
        authorChoiceBox.setItems(FXCollections.observableArrayList(authorGenericRepository.findAll()));
        publisherChoiceBox.setItems(FXCollections.observableArrayList(publisherGenericRepository.findAll()));
    }

    @FXML
    public void onClose()
    {
        ((Stage) errorLabel.getScene().getWindow()).close();
    }
}
