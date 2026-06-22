package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.service.LoanService;

import java.util.Date;
import java.util.List;

public class ReturnBookController {

    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private ComboBox<Loan> loanComboBox;
    @FXML
    private CheckBox damagedCheckBox;
    @FXML
    private Label statusLabel;
    @FXML
    private Label dueDateLabel;
    @FXML
    private Label borrowedDateLabel;

    private static final Logger logger = LogManager.getLogger(ReturnBookController.class);
    private final LoanService loanService = new LoanService();
    private final UserRepository userRepository = new UserRepository();
    private final LoanRepository loanRepository = new LoanRepository();

    @FXML
    private void initialize() {
        loadUsers();
        userComboBox.setOnAction(e -> loadActiveLoans());
        loanComboBox.setOnAction(e -> refreshLoanDetails());
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

    private void loadActiveLoans() {
        User selectedUser = userComboBox.getValue();
        if (selectedUser == null) return;

        List<Loan> activeLoans = loanRepository.findActiveByUser(selectedUser);
        loanComboBox.getItems().setAll(activeLoans);
        loanComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Loan loan) {
                return loan == null ? "" : loan.getInventory().getBook().getTitle()
                                           + " — Copy #" + loan.getInventory().getId();
            }
            @Override
            public Loan fromString(String s) { return null; }
        });

        dueDateLabel.setText("");
        borrowedDateLabel.setText("");
    }

    private void refreshLoanDetails() {
        Loan selectedLoan = loanComboBox.getValue();
        if (selectedLoan == null) return;

        borrowedDateLabel.setText(String.valueOf(selectedLoan.getBorrowedDate()));

        if (selectedLoan.isOverdue()) {
            dueDateLabel.setText(selectedLoan.getDueDate() + " — OVERDUE ("
                    + selectedLoan.getDaysOverdue() + " days)");
            dueDateLabel.setStyle("-fx-text-fill: red;");
        } else {
            dueDateLabel.setText(String.valueOf(selectedLoan.getDueDate()));
            dueDateLabel.setStyle("-fx-text-fill: black;");
        }
    }

    @FXML
    public void onReturn() {
        User selectedUser = userComboBox.getValue();
        Loan selectedLoan = loanComboBox.getValue();

        if (selectedUser == null || selectedLoan == null) {
            statusLabel.setText("Please select a user and a loan");
            return;
        }

        Inventory inventory = selectedLoan.getInventory();

        if (damagedCheckBox.isSelected()) {
            inventory.setStatus(BookStatus.DAMAGED);
        }

        LoanDataDTO data = new LoanDataDTO.Builder(selectedUser, inventory, new Date()).build();
        data.setLoan(selectedLoan);

        try {
            loanService.returnBook(data);
            logger.info("Book returned successfully");
            new Alert(Alert.AlertType.INFORMATION, "Book successfully returned!").show();
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