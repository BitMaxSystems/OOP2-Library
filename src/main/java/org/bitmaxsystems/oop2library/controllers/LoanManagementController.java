package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.loans.enums.LoanStatus;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class LoanManagementController {

    @FXML
    private TableView<Loan> tableView;
    @FXML
    private TableColumn<Loan, String> userColumn;
    @FXML
    private TableColumn<Loan, String> bookColumn;
    @FXML
    private TableColumn<Loan, Long> copyColumn;
    @FXML
    private TableColumn<Loan, Date> borrowedDateColumn;
    @FXML
    private TableColumn<Loan, Date> dueDateColumn;
    @FXML
    private TableColumn<Loan, Date> returnedDateColumn;
    @FXML
    private TableColumn<Loan, LoanStatus> statusColumn;
    @FXML
    private Label overdueCountLabel;
    @FXML
    private ToggleGroup filterToggleGroup;
    @FXML
    private RadioButton allRadio;
    @FXML
    private RadioButton activeRadio;
    @FXML
    private RadioButton overdueRadio;

    private static final Logger logger = LogManager.getLogger(LoanManagementController.class);
    private final LoanRepository loanRepository = new LoanRepository();
    private List<Loan> allLoans;

    @FXML
    private void initialize() {
        userColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getUser().getFirstName()
                        + " " + row.getValue().getUser().getLastName()));

        bookColumn.setCellValueFactory(row ->
                new SimpleStringProperty(row.getValue().getInventory().getBook().getTitle()));

        copyColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getInventory().getId()));

        borrowedDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getBorrowedDate()));

        dueDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getDueDate()));

        returnedDateColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getReturnedDate()));

        statusColumn.setCellValueFactory(row ->
                new SimpleObjectProperty<>(row.getValue().getStatus()));

        dueDateColumn.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty || date == null) {
                    setText(null);
                    setStyle("");
                } else {
                    Loan loan = getTableView().getItems().get(getIndex());
                    setText(String.valueOf(date));
                    if (loan.isOverdue()) {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        filterToggleGroup = new ToggleGroup();
        allRadio.setToggleGroup(filterToggleGroup);
        activeRadio.setToggleGroup(filterToggleGroup);
        overdueRadio.setToggleGroup(filterToggleGroup);
        allRadio.setSelected(true);

        filterToggleGroup.selectedToggleProperty().addListener((obs, oldVal, newVal) -> applyFilter());

        refreshTable();
    }

    private void refreshTable() {
        try {
            allLoans = loanRepository.findAll();
            applyFilter();

            long overdueCount = allLoans.stream()
                    .filter(Loan::isOverdue)
                    .count();

            if (overdueCount > 0) {
                overdueCountLabel.setText("Overdue loans: " + overdueCount);
                overdueCountLabel.setStyle("-fx-text-fill: red;");
            } else {
                overdueCountLabel.setText("No overdue loans");
                overdueCountLabel.setStyle("-fx-text-fill: black;");
            }

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error occurred. Try again").show();
        }
    }

    private void applyFilter() {
        if (allLoans == null) return;

        RadioButton selected = (RadioButton) filterToggleGroup.getSelectedToggle();
        List<Loan> filtered;

        switch (selected.getText()) {
            case "Active" -> filtered = allLoans.stream()
                    .filter(l -> l.getStatus() == LoanStatus.ACTIVE)
                    .toList();
            case "Overdue" -> filtered = allLoans.stream()
                    .filter(Loan::isOverdue)
                    .toList();
            default -> filtered = allLoans;
        }

        tableView.getItems().setAll(filtered);
    }

    @FXML
    public void onBorrowBook() {
        loadDialog(View.BORROW_BOOK);
    }

    @FXML
    public void onReturnBook() {
        loadDialog(View.RETURN_BOOK);
    }

    private void loadDialog(View view) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(view.getPath()));
            AnchorPane root = loader.load();

            Stage stage = new Stage();
            stage.setTitle(view.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, "Unexpected error, try again!").show();
        }
    }
}