package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.util.chain.book.CreateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.VerifyBookDataChain;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

public class BookRegistryCreationController extends BaseBookRegistryFormController {

    @FXML
    private TextField isbnField;

    @FXML
    public void onCreate()
    {

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(isbnField.getText().strip(),
                titleField.getText().strip(),
                genreChoiceBox.getValue(),
                authorChoiceBox.getValue(),
                publisherChoiceBox.getValue()).build();

        resetErrorLabel();
        try
        {
            bookService.createBook(bookDataDTO);
            new Alert(Alert.AlertType.INFORMATION, "Book is created!").show();
            onClose();
        }
        catch (DataValidationException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            setErrors(e.getMessage());

        } catch (DataAlreadyExistException e) {
            new Alert(Alert.AlertType.ERROR, "Book with this ISBN already exists").show();
            setErrors(e.getMessage());
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }

    }
}
