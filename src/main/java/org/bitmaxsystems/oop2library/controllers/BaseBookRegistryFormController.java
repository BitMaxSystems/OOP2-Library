package org.bitmaxsystems.oop2library.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class BaseBookRegistryFormController {
    @FXML
    protected Label errorLabel;
//    @FXML
//    private TextField isbnField;
    @FXML
    protected TextField titleField;
    @FXML
    protected ChoiceBox<Genre> genreChoiceBox;
    @FXML
    protected ChoiceBox<Author> authorChoiceBox;
    @FXML
    protected ChoiceBox<Publisher> publisherChoiceBox;
    private GenericRepository<Genre> genreGenericRepository = new GenericRepository<>(Genre.class);
    private GenericRepository<Author> authorGenericRepository = new GenericRepository<>(Author.class);
    private GenericRepository<Publisher> publisherGenericRepository = new GenericRepository<>(Publisher.class);

    protected void resetErrorLabel() {
        String string =
                "- ISBN is 13 digits in format: xxx-x-xx-xxxxxx-x. Example: 1234-5-67-891234-5.";
        errorLabel.setTextFill(Color.BLACK);
        errorLabel.setText(string);
    }

    protected void setErrors(String errors) {
        errorLabel.setTextFill(Color.RED);
        errorLabel.setText(errors);
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
