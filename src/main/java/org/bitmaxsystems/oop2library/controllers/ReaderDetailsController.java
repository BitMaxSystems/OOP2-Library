package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.services.LibraryService;

import java.time.LocalDate;
import java.util.Objects;

public class ReaderDetailsController extends UserDetailsController{

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

    private LibraryService libraryService = new LibraryService();


    @Override
    protected void initialize()
    {
        super.initialize();

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

    }

    @Override
    public void setUser(User user) {
        super.setUser(user);
        if (Objects.nonNull(this.user))
        {
            refreshTable();
        }
    }

    private void refreshTable() {
        try {
            tableView.getItems().setAll(libraryService.getActiveHistoryForUser(user));
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error occurred. Try again."
            ).show();
        }
    }
}
