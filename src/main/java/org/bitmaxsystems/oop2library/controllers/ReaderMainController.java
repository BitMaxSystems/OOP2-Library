package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.reservations.Reservation;
import org.bitmaxsystems.oop2library.models.reservations.enums.ReservationStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.repository.ReservationRepository;
import org.bitmaxsystems.oop2library.util.UserManager;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class ReaderMainController {

    @FXML
    private Button userDetailsButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> bookTableView;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> genreColumn;
    @FXML
    private TableColumn<Book, String> publisherColumn;
    @FXML
    private TableColumn<Book, String> availabilityColumn;
    @FXML
    private TableView<Loan> loanTableView;
    @FXML
    private TableColumn<Loan, String> loanBookColumn;
    @FXML
    private TableColumn<Loan, Date> loanBorrowedDateColumn;
    @FXML
    private TableColumn<Loan, Date> loanDueDateColumn;
    @FXML
    private TableColumn<Loan, String> loanStatusColumn;
    @FXML
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, String> reservationBookColumn;
    @FXML
    private TableColumn<Reservation, Date> reservationDateColumn;
    @FXML
    private TableColumn<Reservation, String> reservationStatusColumn;
    @FXML
    private Label loyaltyPointsLabel;

    private static final Logger logger = LogManager.getLogger(ReaderMainController.class);
    private final UserManager manager = UserManager.getInstance();
    private final GenericRepository<Book> bookRepository = new GenericRepository<>(Book.class);
    private final GenericRepository<Inventory> inventoryRepository = new GenericRepository<>(Inventory.class);
    private final LoanRepository loanRepository = new LoanRepository();
    private final ReservationRepository reservationRepository = new ReservationRepository();
    private List<Book> allBooks;

    @FXML
    private void initialize() {
        User user = manager.getLoggedUser();
        userDetailsButton.setText(user.getFirstName() + " " + user.getLastName());
        loyaltyPointsLabel.setText("Loyalty points: " + user.getLoyaltyPoints());

        setupBookTable();
        setupLoanTable();
        setupReservationTable();

        searchField.textProperty().addListener((obs, oldVal, newVal) -> applySearch(newVal));

        refreshAll();
    }

    private void setupBookTable() {
        titleColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getTitle()));
        authorColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getAuthor().getName()));
        genreColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getGenre().getName()));
        publisherColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getPublisher().getName()));

        availabilityColumn.setCellValueFactory(row -> {
            Book book = row.getValue();
            boolean hasAvailable = inventoryRepository.findAll()
                    .stream()
                    .anyMatch(inv -> inv.getBook().getId().equals(book.getId())
                            && inv.getStatus() == BookStatus.AVAILABLE);
            return new SimpleStringProperty(hasAvailable ? "Available" : "Unavailable");
        });

        availabilityColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(value);
                    setStyle(value.equals("Available")
                            ? "-fx-text-fill: green;"
                            : "-fx-text-fill: red;");
                }
            }
        });
    }

    private void setupLoanTable() {
        loanBookColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getInventory().getBook().getTitle()));
        loanBorrowedDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getBorrowedDate()));
        loanDueDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getDueDate()));

        loanStatusColumn.setCellValueFactory(row -> {
            Loan loan = row.getValue();
            String status = loan.isOverdue()
                    ? "OVERDUE (" + loan.getDaysOverdue() + " days)"
                    : loan.getStatus().toString();
            return new SimpleStringProperty(status);
        });

        loanStatusColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String value, boolean empty) {
                super.updateItem(value, empty);
                if (empty || value == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(value);
                    setStyle(value.startsWith("OVERDUE")
                            ? "-fx-text-fill: red; -fx-font-weight: bold;"
                            : "-fx-text-fill: black;");
                }
            }
        });
    }

    private void setupReservationTable() {
        reservationBookColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getBook().getTitle()));
        reservationDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getReservationDate()));
        reservationStatusColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getStatus().toString()));
    }

    private void refreshAll() {
        try {
            User user = manager.getLoggedUser();

            allBooks = bookRepository.findAll();
            bookTableView.getItems().setAll(allBooks);

            List<Loan> loans = loanRepository.findAllByUser(user);
            loanTableView.getItems().setAll(loans);

            List<Reservation> reservations = reservationRepository.findAllByUser(user);
            reservationTableView.getItems().setAll(reservations);

            loyaltyPointsLabel.setText("Loyalty points: " + user.getLoyaltyPoints());

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred. Try again").show();
        }
    }

    private void applySearch(String query) {
        if (allBooks == null) return;

        if (query == null || query.isBlank()) {
            bookTableView.getItems().setAll(allBooks);
            return;
        }

        String lower = query.toLowerCase();
        List<Book> filtered = allBooks.stream()
                .filter(b -> b.getTitle().toLowerCase().contains(lower)
                        || b.getAuthor().getName().toLowerCase().contains(lower)
                        || b.getGenre().getName().toLowerCase().contains(lower)
                        || b.getPublisher().getName().toLowerCase().contains(lower))
                .collect(Collectors.toList());

        bookTableView.getItems().setAll(filtered);
    }

    @FXML
    public void onReserve() {
        Book selectedBook = bookTableView.getSelectionModel().getSelectedItem();
        if (selectedBook == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a book to reserve").show();
            return;
        }

        User user = manager.getLoggedUser();

        Reservation existing = reservationRepository.findPendingByUserAndBook(user, selectedBook);
        if (existing != null) {
            new Alert(Alert.AlertType.WARNING, "You already have a pending reservation for this book").show();
            return;
        }

        boolean hasAvailable = inventoryRepository.findAll()
                .stream()
                .anyMatch(inv -> inv.getBook().getId().equals(selectedBook.getId())
                        && inv.getStatus() == BookStatus.AVAILABLE);

        if (hasAvailable) {
            new Alert(Alert.AlertType.WARNING, "This book is currently available — borrow it instead of reserving").show();
            return;
        }

        try {
            Reservation reservation = new Reservation(selectedBook, user);
            reservationRepository.save(reservation);
            new Alert(Alert.AlertType.INFORMATION, "Reservation placed successfully!").show();
            logger.info("Reservation placed for book {}", selectedBook.getTitle());
            refreshAll();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onCancelReservation() {
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();
        if (selectedReservation == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a reservation to cancel").show();
            return;
        }

        if (selectedReservation.getStatus() != ReservationStatus.PENDING) {
            new Alert(Alert.AlertType.WARNING, "Only pending reservations can be cancelled").show();
            return;
        }

        try {
            Reservation managed = reservationRepository.findById(selectedReservation.getId());
            managed.cancel();
            reservationRepository.update(managed);
            new Alert(Alert.AlertType.INFORMATION, "Reservation cancelled").show();
            logger.info("Reservation cancelled for book {}", selectedReservation.getBook().getTitle());
            refreshAll();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onUserDetails() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource(View.USER_DETAILS.getPath()));
            javafx.scene.layout.AnchorPane root = loader.load();

            UserDetailsController controller = loader.getController();
            controller.setUser(manager.getLoggedUser());

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle(View.USER_DETAILS.getTitle());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(new javafx.scene.Scene(root));
            stage.showAndWait();
            refreshAll();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error, try again!").show();
        }
    }

    @FXML
    public void onLogout() {
        logger.info("Reader logged out");
        UserManager.getInstance().logoff();
        SceneManager.showView(View.LOGIN);
    }
}