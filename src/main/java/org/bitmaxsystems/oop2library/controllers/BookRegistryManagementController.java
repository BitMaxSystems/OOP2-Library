package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.util.bookformchain.UpdateBookChain;
import org.bitmaxsystems.oop2library.util.bookformchain.VerifyBookDataChain;
import org.bitmaxsystems.oop2library.util.contracts.IBookFormChain;
import org.bitmaxsystems.oop2library.util.service.bookService.DeleteBookService;


import java.util.Optional;

public class BookRegistryManagementController extends BaseBookRegistryFormController {
    @FXML
    private Label isbnLabel;
    @FXML
    private Label headerLabel;
    private Book book;
    private static final Logger logger = LogManager.getLogger(BookRegistryManagementController.class);


    public void setBook(Book book) {
        this.book = book;
        headerLabel.setText("Manage " + book.getTitle());
        isbnLabel.setText(book.getIsbn());
        titleField.setText(book.getTitle());
        genreChoiceBox.setValue(book.getGenre());
        authorChoiceBox.setValue(book.getAuthor());
        publisherChoiceBox.setValue(book.getPublisher());
    }

    @Override
    protected void resetErrorLabel() {
        errorLabel.setTextFill(Color.BLACK);
        errorLabel.setText("");
    }

    @FXML
    private void onUpdate() {
        IBookFormChain verifyData = new VerifyBookDataChain();
        IBookFormChain updateBook = new UpdateBookChain();

        verifyData.setNextChain(updateBook);

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(isbnLabel.getText().strip(),
                titleField.getText().strip(),
                genreChoiceBox.getValue(),
                authorChoiceBox.getValue(),
                publisherChoiceBox.getValue()).setBook(book).build();

        resetErrorLabel();
        try {
            verifyData.execute(bookDataDTO);
            new Alert(Alert.AlertType.INFORMATION, "Book is updated!").show();
            logger.info("Book is updated!");
            onClose();
        } catch (DataValidationException e) {
            logger.error("Invalid data inputted");
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            setErrors(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }

    }

    @FXML
    private void onDelete() {
        String bookTitle = book.getTitle();
        Optional<ButtonType> alertResult = new Alert(Alert.AlertType.WARNING,
                "Are you sure you want to delete " + bookTitle,
                ButtonType.YES,
                ButtonType.NO)
                .showAndWait();

        if (alertResult.isPresent() && alertResult.get() == ButtonType.YES) {
            DeleteBookService deleteBookService = new DeleteBookService();

            try {
                boolean deleted = deleteBookService.deleteBook(book);

                if (!deleted) {
                    new Alert(Alert.AlertType.WARNING, "Cannot delete " + bookTitle + "because inventory copies still exist.")
                            .show();

                    logger.warn("Could not delete {} because inventory copies still exist.", bookTitle);
                    return;
                }

                new Alert(Alert.AlertType.INFORMATION, bookTitle + " successfully deleted!").show();

                logger.info("{} successfully deleted!", bookTitle);
                onClose();
            } catch (ChildRecordExistException e) {
                logger.error(e);
                new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
            }
            catch (Exception e) {
                logger.error(e);
                new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
            }
        }
    }
}
