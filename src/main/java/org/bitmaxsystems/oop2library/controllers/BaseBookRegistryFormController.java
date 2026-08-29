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
import org.bitmaxsystems.oop2library.services.BookParameterService;
import org.bitmaxsystems.oop2library.services.BookService;

public class BaseBookRegistryFormController {
    @FXML
    protected Label errorLabel;

    @FXML
    protected TextField titleField;
    @FXML
    protected ChoiceBox<Genre> genreChoiceBox;
    @FXML
    protected ChoiceBox<Author> authorChoiceBox;
    @FXML
    protected ChoiceBox<Publisher> publisherChoiceBox;

    private BookParameterService bookParameterService = new BookParameterService();
    protected BookService bookService = new BookService();


    protected void resetErrorLabel() {
        String string =
                "- ISBN is 13 digits in format: xxx-x-xx-xxxxxx-x. Example: 123-4-56-789123-4.";
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
        genreChoiceBox.setItems(FXCollections.observableArrayList(bookParameterService.findAllParameterRecords(Genre.class)));
        authorChoiceBox.setItems(FXCollections.observableArrayList(bookParameterService.findAllParameterRecords(Author.class)));
        publisherChoiceBox.setItems(FXCollections.observableArrayList(bookParameterService.findAllParameterRecords(Publisher.class)));
    }

    @FXML
    public void onClose()
    {
        ((Stage) errorLabel.getScene().getWindow()).close();
    }
}
