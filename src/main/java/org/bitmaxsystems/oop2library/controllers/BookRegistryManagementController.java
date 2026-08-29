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
import org.bitmaxsystems.oop2library.services.BookService;
import org.bitmaxsystems.oop2library.services.bookService.DeleteBookService;
import org.bitmaxsystems.oop2library.util.chain.book.UpdateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.VerifyBookDataChain;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

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
        BookDataDTO bookDataDTO = new BookDataDTO.Builder(isbnLabel.getText().strip(),
                titleField.getText().strip(),
                genreChoiceBox.getValue(),
                authorChoiceBox.getValue(),
                publisherChoiceBox.getValue()).setBook(book).build();

        resetErrorLabel();

        try {
            bookService.updateBook(bookDataDTO);
            new Alert(Alert.AlertType.INFORMATION, "Book is updated!").show();
            onClose();

        } catch (DataValidationException e) {
            new Alert(Alert.AlertType.ERROR, "Invalid data found").show();
            setErrors(e.getMessage());
        } catch (ChildRecordExistException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        catch (Exception e) {
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
                deleteBookService.deleteBook(book);

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
