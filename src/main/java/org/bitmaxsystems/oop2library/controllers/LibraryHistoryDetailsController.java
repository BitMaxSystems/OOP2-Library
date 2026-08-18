package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.services.LibraryService;

import java.util.Objects;

public class LibraryHistoryDetailsController {
    private History history;

    @FXML
    private Label headerLabel;

    @FXML
    private Label inventoryIdLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label archivedLabel;

    @FXML
    private Label dateOfLendingLabel;

    @FXML
    private Label expectedDateOfReturnLabel;

    @FXML
    private Label actualDateOfReturnLabel;

    @FXML
    private Label lendStatusLabel;

    @FXML
    private Button returnButton;

    private final LibraryService libraryService = new LibraryService();



    public void setHistory(History history) {
        this.history = history;
        updateControls();

    }

    private void updateControls()
    {
        headerLabel.setText("History Record: "+ history.getId());
        inventoryIdLabel.setText(String.valueOf(history.getInventory().getId()));
        isbnLabel.setText(history.getInventory().getBook().getIsbn());
        titleLabel.setText(history.getInventory().getBook().getTitle());
        authorLabel.setText(history.getInventory().getBook().getAuthor().getName());
        genreLabel.setText(history.getInventory().getBook().getGenre().getName());
        publisherLabel.setText(history.getInventory().getBook().getPublisher().getName());
        archivedLabel.setText(history.getInventory().isArchived() ? "Archived" : "Not archived");
        dateOfLendingLabel.setText(history.getDateOfLending().toString());
        expectedDateOfReturnLabel.setText(history.getExpectedReturnDate().toString());
        actualDateOfReturnLabel.setText(Objects.isNull(history.getActualReturnDate())? "Not returned" : history.getActualReturnDate().toString());

        if (history.getLendStatus() == LendStatusEnum.OVERDUE)
        {
            lendStatusLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        }
        else
        {
            lendStatusLabel.setStyle("");
        }
        lendStatusLabel.setText(history.getLendStatus().toString());

        if (history.getLendStatus() == LendStatusEnum.RETURNED || history.getLendStatus() == LendStatusEnum.RETURNED_OVERDUE )
        {
            returnButton.setText("Returned");
            returnButton.setDisable(true);
        }
    }

    @FXML
    public void onReturn()
    {
        try{
            libraryService.returnBook(history);
            new Alert(Alert.AlertType.INFORMATION
                    ,"Book was successfully returned").show();
        }
        catch (IllegalStateException e)
        {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (Exception e)
        {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
        finally {
            onClose();
        }
    }


    @FXML
    public void onClose()
    {
        ((Stage) headerLabel.getScene().getWindow()).close();
    }
}
