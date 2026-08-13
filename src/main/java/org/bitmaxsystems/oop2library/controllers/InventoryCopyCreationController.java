package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.service.inventoryService.CreateInventoryCopiesService;

public class InventoryCopyCreationController {

    @FXML
    private ComboBox<Book> bookComboBox;

    @FXML
    private Spinner<Integer> quantitySpinner;

    private final GenericRepository<Book> bookRepository =
            new GenericRepository<>(Book.class);

    private final CreateInventoryCopiesService createInventoryCopiesService =
            new CreateInventoryCopiesService();

    @FXML
    private void initialize() {
        bookComboBox.getItems().setAll(bookRepository.findAll());

        bookComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Book book) {
                if (book == null) {
                    return "";
                }

                return book.getTitle() + " | " + book.getIsbn();
            }

            @Override
            public Book fromString(String string) {
                return null;
            }
        });

        quantitySpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(
                        1,
                        100,
                        1
                )
        );
    }

    @FXML
    private void onCreate() {
        Book selectedBook = bookComboBox.getValue();

        if (selectedBook == null) {
            return;
        }

        int quantity = quantitySpinner.getValue();

        createInventoryCopiesService.createCopies(
                selectedBook,
                quantity
        );

        closeWindow();
    }

    @FXML
    private void onCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage =
                (Stage) bookComboBox.getScene().getWindow();

        stage.close();
    }
}