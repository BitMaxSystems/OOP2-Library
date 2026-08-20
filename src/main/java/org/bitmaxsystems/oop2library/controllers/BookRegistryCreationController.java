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

    private static final Logger logger = LogManager.getLogger(BookRegistryCreationController.class);


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
}
