package org.bitmaxsystems.oop2library.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.bookformchain.CreateBookChain;
import org.bitmaxsystems.oop2library.util.bookformchain.VerifyBookDataChain;
import org.bitmaxsystems.oop2library.util.contracts.IBookFormChain;

public class BookRegistryCreationControl {
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
    private static final Logger logger = LogManager.getLogger(BookRegistryCreationControl.class);
    private GenericRepository<Genre> genreGenericRepository = new GenericRepository<>(Genre.class);
    private GenericRepository<Author> authorGenericRepository = new GenericRepository<>(Author.class);
    private GenericRepository<Publisher> publisherGenericRepository = new GenericRepository<>(Publisher.class);

    private void resetErrorLabel() {
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
    public void onCreate()
    {
        IBookFormChain verifyData = new VerifyBookDataChain();
        IBookFormChain createBook = new CreateBookChain();

        verifyData.setNextChain(createBook);

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(isbnField.getText().strip(),
                titleField.getText().strip(),
                genreChoiceBox.getValue(),
                authorChoiceBox.getValue(),
                publisherChoiceBox.getValue()).build();

        resetErrorLabel();
        try
        {
            verifyData.execute(bookDataDTO);
            new Alert(Alert.AlertType.INFORMATION, "Book is created!").show();
            logger.info("Book is created!");
            onClose();
        }
        catch (DataValidationException e) {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            setErrors(e.getMessage());

        } catch (DataAlreadyExistException e) {
            logger.error("Book with this ISBN already exists");
            new Alert(Alert.AlertType.ERROR, "Book with this ISBN already exists").show();
            setErrors(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }

    }

    @FXML
    public void onClose()
    {
        ((Stage) errorLabel.getScene().getWindow()).close();
    }
}
