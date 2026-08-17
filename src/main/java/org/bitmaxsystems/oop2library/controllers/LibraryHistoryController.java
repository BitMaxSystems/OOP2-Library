package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.HistoryRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class LibraryHistoryController {
    @FXML
    private TableView<History> tableView;

    @FXML
    private TableColumn<History, LocalDate> dateOfLendingColumn;

    @FXML
    private TableColumn<History, Integer> inventoryIdColumn;

    @FXML
    private TableColumn<History, String> lendStatusColumn;

    @FXML
    private TableColumn<History, Boolean> archivedColumn;

    @FXML
    private TableColumn<History, String> isbnColumn;

    @FXML
    private TableColumn<History, String> titleColumn;

    @FXML
    private TableColumn<History, String> authorColumn;

    @FXML
    private TableColumn<History, String> genreColumn;

    @FXML
    private TableColumn<History, String> publisherColumn;

    @FXML
    private TableColumn<History, LocalDate> expectedReturnDateColumn;

    @FXML
    private TableColumn<History, LocalDate> actualReturnDateColumn;

    @FXML
    private ChoiceBox<User> userChoiceBox;

    private static final Logger logger =
            LogManager.getLogger(LibraryHistoryController.class);

    private final HistoryRepository historyRepository =
            HistoryRepository.getInstance();

    private final UserRepository userRepository = UserRepository.getInstance();

    private User selectedUser;

    @FXML
    private void initialize() {

        dateOfLendingColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(
                cellData.getValue().getDateOfLending()
                )
        );

        inventoryIdColumn.setCellValueFactory( cellData ->
                new SimpleObjectProperty<>( cellData.getValue().getInventory().getId())
        );

        lendStatusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getLendStatus().toString()
                )
        );

        lendStatusColumn.setCellFactory(col -> new TableCell<>()
        {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item.isBlank())
                {
                    setText("");
                    setStyle("");
                }
                else
                {
                    LendStatusEnum statusEnum = getTableView().getItems().get(getIndex()).getLendStatus();
                    setText(statusEnum.toString());

                    if (statusEnum == LendStatusEnum.OVERDUE)
                    {
                        setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                    }
                    else
                    {
                        setStyle("");
                    }
                }
            }
        });

        archivedColumn.setCellValueFactory( cellData ->
                new SimpleBooleanProperty(
                        cellData.getValue().getInventory().isArchived()
                )
        );

        archivedColumn.setCellFactory(column -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            {
                checkBox.setDisable(true);
            }

            @Override
            protected void updateItem(Boolean archived, boolean empty) {
                super.updateItem(archived, empty);

                if (empty || archived == null) {
                    setGraphic(null);
                    return;
                }

                checkBox.setSelected(archived);
                setGraphic(checkBox);
            }
        });

        isbnColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInventory().getBook().getIsbn()
                )
        );

        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInventory().getBook().getTitle()
                )
        );

        authorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInventory().getBook().getAuthor().toString()
                )
        );

        genreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInventory().getBook().getGenre().toString()
                )
        );

        publisherColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getInventory().getBook().getPublisher().toString()
                )
        );

        expectedReturnDateColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(
                        cellData.getValue().getExpectedReturnDate()
                )
        );

        actualReturnDateColumn.setCellValueFactory( cellData -> new SimpleObjectProperty<>(
                        cellData.getValue().getActualReturnDate()
                )
        );

        userChoiceBox.setItems(FXCollections.observableArrayList(userRepository.searchByRole(UserRole.READER)));

    }

    @FXML
    public void onBackToMainDialog() {
        SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
    }

    @FXML
    public void onChangeUser()
    {
        this.selectedUser = userChoiceBox.getValue();
        refreshTable();
    }

    private void refreshTable() {
        try {
            tableView.getItems().setAll(historyRepository.searchByUser(selectedUser));
            logger.info("Loaded lend history for {} {}", selectedUser.getFirstName(), selectedUser.getLastName());
        } catch (Exception e) {
            logger.error(e);
            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error occurred. Try again."
            ).show();
        }
    }

    @FXML
    public void onCreateLendHistory()
    {
        new Alert(Alert.AlertType.INFORMATION,"Placeholder").show();
    }
}
