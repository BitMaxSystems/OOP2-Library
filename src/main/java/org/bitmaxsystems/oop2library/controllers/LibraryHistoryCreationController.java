package org.bitmaxsystems.oop2library.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.InventoryRepository;
import org.bitmaxsystems.oop2library.services.LibraryService;

import java.util.Objects;

public class LibraryHistoryCreationController {
    private User selectedUser;
    private Inventory selectedInventoryBook;

    @FXML
    private Label headerLabel;

    @FXML
    private Label loyaltyPointsLabel;

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
    private ChoiceBox<Inventory> availableBooksChoiceBox;

    private InventoryRepository inventoryRepository = InventoryRepository.getInstance();
    private LibraryService libraryService = new LibraryService();

    @FXML
    public void initialize()
    {
        availableBooksChoiceBox.setItems(FXCollections.observableArrayList(inventoryRepository.getInventoryByState(InventoryStateEnum.AVAILABLE)));
        availableBooksChoiceBox.setOnAction(event -> {
            onSelectInventoryBook();
        });
    }

    public void setSelectedUser(User user)
    {
        this.selectedUser = user;
        updateUserLabels();
    }

    private void updateUserLabels()
    {
        headerLabel.setText("Lend book to: "+selectedUser.getFirstName()+" "+selectedUser.getLastName());
        loyaltyPointsLabel.setText(selectedUser.getLoyaltyPoints()+"/100");
    }

    private void updateInventoryLabels()
    {
        inventoryIdLabel.setText(String.valueOf(selectedInventoryBook.getId()));
        isbnLabel.setText(selectedInventoryBook.getBook().getIsbn());
        titleLabel.setText(selectedInventoryBook.getBook().getTitle());
        authorLabel.setText(selectedInventoryBook.getBook().getAuthor().getName());
        genreLabel.setText(selectedInventoryBook.getBook().getGenre().getName());
        publisherLabel.setText(selectedInventoryBook.getBook().getPublisher().getName());
        archivedLabel.setText(selectedInventoryBook.isArchived() ? "Archived" : "Not archived");

        loyaltyPointsLabel.setText(selectedUser.getLoyaltyPoints()+"/100");
    }

    @FXML
    public void onLendInside()
    {
        try {
            if (Objects.isNull(selectedInventoryBook))
            {
                throw new IllegalStateException("No book selected");
            }

            libraryService.lendInside(selectedUser,selectedInventoryBook);
            new Alert(Alert.AlertType.INFORMATION
                    ,"Book was successfully lent inside until the end of the day").show();

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
    public void onLendOutside()
    {
        try {
            if (Objects.isNull(selectedInventoryBook))
            {
                throw new IllegalStateException("No book selected");
            }

            libraryService.lendOutside(selectedUser,selectedInventoryBook);
            new Alert(Alert.AlertType.INFORMATION
                    ,"Book was successfully lent outside for 1 month").show();
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
    public void onSelectInventoryBook()
    {
        this.selectedInventoryBook = availableBooksChoiceBox.getValue();
        updateInventoryLabels();

    }

    @FXML
    public void onClose()
    {
        ((Stage) headerLabel.getScene().getWindow()).close();
    }
}
