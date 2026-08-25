package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.HistoryRepository;
import org.bitmaxsystems.oop2library.repository.UserRepository;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

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

    private final HistoryRepository historyRepository =
            HistoryRepository.getInstance();

    private final UserRepository userRepository = UserRepository.getInstance();

    private static final Logger logger = LogManager.getLogger(LibraryHistoryController.class);


    private User selectedUser;

    private void loadUsers()
    {
        if(userChoiceBox.getItems().isEmpty())
        {
            userChoiceBox.setItems(FXCollections.observableArrayList(userRepository.searchByRole(UserRole.READER)));

        }
        else
        {
            User selectedUser = userChoiceBox.getValue();
            List<User> refreshedList = userRepository.searchByRole(UserRole.READER);
            userChoiceBox.getItems().setAll(refreshedList);
            userChoiceBox.setValue(selectedUser);
        }
    }

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

        loadUsers();

        tableView.setOnMouseClicked(this::onTableClick);

    }

    @FXML
    public void onBackToMainDialog() {
        SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
    }

    @FXML
    public void onChangeUser()
    {
        this.selectedUser = userChoiceBox.getValue();
        if (Objects.nonNull(selectedUser))
        {
            refreshTable();
        }
    }

    private void loadHistoryDetails(History history)
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.LIBRARY_HISTORY_DETAILS_VIEW.getPath()));
            AnchorPane root = loader.load();

            LibraryHistoryDetailsController controller = loader.getController();
            controller.setHistory(history);

            Stage stage = new Stage();
            stage.setTitle(View.LIBRARY_HISTORY_DETAILS_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            tableView.getItems().clear();
            loadUsers();
            refreshTable();

        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    private void onTableClick(MouseEvent event)
    {
        if (event.getClickCount() == 2)
        {
            History selectedHistory = tableView.getSelectionModel().getSelectedItem();
            if (selectedHistory != null)
            {
                loadHistoryDetails(selectedHistory);
            }
        }
    }

    private void refreshTable() {
        try {
            tableView.getItems().setAll(historyRepository.searchByUser(selectedUser));
        } catch (Exception e) {
            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error occurred. Try again."
            ).show();
        }
    }

    @FXML
    public void onCreateLendHistory()
    {
        try {
            User selectedUser = userChoiceBox.getValue();

            if (Objects.isNull(selectedUser))
            {
                throw new IllegalArgumentException("No user selected");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.LIBRARY_HISTORY_CREATION_VIEW.getPath()));
            AnchorPane root = loader.load();

            LibraryHistoryCreationController controller = loader.getController();
            controller.setSelectedUser(selectedUser);

            Stage stage = new Stage();
            stage.setTitle(View.LIBRARY_HISTORY_CREATION_VIEW.getTitle());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshTable();

        } catch (IllegalArgumentException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }
}
