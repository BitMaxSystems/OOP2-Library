package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.service.LoanService;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BorrowBookController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<Book> bookComboBox;
    @FXML
    private ComboBox<Inventory> inventoryComboBox;
    @FXML
    private DatePicker dueDatePicker;
    @FXML
    private Label statusLabel;

    private static final Logger logger = LogManager.getLogger(BorrowBookController.class);
    private final LoanService loanService = new LoanService();
    private final UserRepository userRepository = new UserRepository();
    private final GenericRepository<Book> bookRepository = new GenericRepository<>(Book.class);
    private final GenericRepository<Inventory> inventoryRepository = new GenericRepository<>(Inventory.class);

    @FXML
    private void initialize() {
        loadUsers();
        loadBooks();

        bookComboBox.setOnAction(e -> loadAvailableCopies());
        dueDatePicker.setValue(LocalDate.now().plusWeeks(2));
    }

    private void loadUsers() {
        List<User> readers = userRepository.searchByRole(UserRole.READER);
        userComboBox.getItems().setAll(readers);
        userComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(User user) {
                return user == null ? "" : user.getFirstName() + " " + user.getLastName();
            }
            @Override
            public User fromString(String s) { return null; }
        });
    }

    private void loadBooks() {
        List<Book> books = bookRepository.findAll();
        bookComboBox.getItems().setAll(books);
        bookComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Book book) {
                return book == null ? "" : book.getTitle() + " — " + book.getAuthor().getName();
            }

            @Override
            public Book fromString(String s) {
                return null;
            }
        });
    }

    private void loadAvailableCopies() {
        Book selectedBook = bookComboBox.getValue();
        if (selectedBook == null) return;

        List<Inventory> availableCopies = inventoryRepository.findAll()
                .stream()
                .filter(inv -> inv.getBook().getId().equals(selectedBook.getId()))
                .filter(inv -> inv.getStatus() == BookStatus.AVAILABLE)
                .collect(Collectors.toList());

        inventoryComboBox.getItems().setAll(availableCopies);
        inventoryComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Inventory inv) {
                return inv == null ? "" : "Copy #" + inv.getId();
            }
            @Override
            public Inventory fromString(String s) { return null; }
        });
    }

    @FXML
    public void onBorrow() {
        User selectedUser = userComboBox.getValue();
        Inventory selectedInventory = inventoryComboBox.getValue();
        LocalDate dueLocalDate = dueDatePicker.getValue();

        if (selectedUser == null || selectedInventory == null || dueLocalDate == null) {
            statusLabel.setText("Please fill in all fields");
            return;
        }

        Date dueDate = Date.from(dueLocalDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

        LoanDataDTO data = new LoanDataDTO.Builder(selectedUser, selectedInventory, dueDate).build();

        try {
            loanService.borrowBook(data);
            logger.info("Book borrowed successfully");
            new Alert(Alert.AlertType.INFORMATION, "Book successfully borrowed!").show();
            ((Stage) statusLabel.getScene().getWindow()).close();

        } catch (DataValidationException e) {
            logger.error(e.getMessage());
            statusLabel.setText(e.getMessage());
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred, try again.").show();
        }
    }

    @FXML
    public void onClose() {
        ((Stage) statusLabel.getScene().getWindow()).close();
    }
}