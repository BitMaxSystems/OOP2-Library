package org.bitmaxsystems.oop2library.controllers;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.services.inventoryService.ArchiveInventoryCopyService;
import org.bitmaxsystems.oop2library.services.inventoryService.DeleteInventoryCopyService;
import org.bitmaxsystems.oop2library.view.SceneManager;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class AdministrativeInventoryController {

    @FXML
    private TableView<Inventory> tableView;

    @FXML
    private TableColumn<Inventory, Integer> inventoryIdColumn;

    @FXML
    private TableColumn<Inventory, String> statusColumn;

    @FXML
    private TableColumn<Inventory, Boolean> archivedColumn;

    @FXML
    private TableColumn<Inventory, String> isbnColumn;

    @FXML
    private TableColumn<Inventory, String> titleColumn;

    @FXML
    private TableColumn<Inventory, String> authorColumn;

    @FXML
    private TableColumn<Inventory, String> genreColumn;

    @FXML
    private TableColumn<Inventory, String> publisherColumn;

    @FXML
    private TableColumn<Inventory, Void> archiveColumn;

    @FXML
    private TableColumn<Inventory, Void> deleteColumn;

    private static final Logger logger =
            LogManager.getLogger(AdministrativeInventoryController.class);

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    private final ArchiveInventoryCopyService archiveInventoryCopyService =
            new ArchiveInventoryCopyService();

    private final DeleteInventoryCopyService deleteInventoryCopyService =
            new DeleteInventoryCopyService();

    @FXML
    private void initialize() {
        inventoryIdColumn.setCellValueFactory(
                new PropertyValueFactory<>("id")
        );

        statusColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getState().getStateEnum().toString()
                )
        );

        archivedColumn.setCellValueFactory( cellData ->
                new SimpleBooleanProperty(
                        cellData.getValue().isArchived()
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
                        cellData.getValue().getBook().getIsbn()
                )
        );

        titleColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getBook().getTitle()
                )
        );

        authorColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getBook().getAuthor().toString()
                )
        );

        genreColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getBook().getGenre().toString()
                )
        );

        publisherColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getBook().getPublisher().toString()
                )
        );

        archiveColumn.setCellFactory(column -> new TableCell<>() {

            private final Button archiveButton = new Button("Archive");

            {
                archiveButton.setOnAction(event -> {
                    Inventory inventory = getTableView()
                            .getItems()
                            .get(getIndex());

                    try {
                        archiveInventoryCopyService.archive(inventory);
                        refreshTable();
                    } catch (IllegalStateException e) {
                        new Alert(
                                Alert.AlertType.WARNING,
                                e.getMessage()
                        ).show();
                    }

                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    return;
                }

                Inventory inventory = getTableView()
                        .getItems()
                        .get(getIndex());

                archiveButton.setDisable(
                        inventory.isArchived() || !(inventory.getState() instanceof AvailableInventoryState)
                );

                setGraphic(archiveButton);
            }
        });

        deleteColumn.setCellFactory(column -> new TableCell<>() {

            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    Inventory inventory = getTableView()
                            .getItems()
                            .get(getIndex());

                    Alert confirmation = new Alert(
                            Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete inventory copy #"
                                    + inventory.getId() + "?"
                    );

                    confirmation.setHeaderText("Delete inventory copy");

                    confirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            deleteInventoryCopyService.delete(inventory);
                            refreshTable();
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

        refreshTable();
    }

    private void refreshTable() {
        try {
            tableView.getItems().setAll(inventoryRepository.findAll());
        } catch (Exception e) {
            logger.error(e);
            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error occurred. Try again."
            ).show();
        }
    }

    @FXML
    public void onBackToMainDialog() {
        SceneManager.showView(View.ADMINISTRATIVE_HOME_VIEW);
    }

    @FXML
    public void onViewRegisteredBooks() {
        SceneManager.showView(View.ADMINISTRATIVE_BOOK_REGISTRY_VIEW);
    }

    @FXML
    public void onAddCopies() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            View.INVENTORY_COPY_CREATION_VIEW.getPath()
                    )
            );

            AnchorPane root = loader.load();

            Stage stage = new Stage();

            stage.setTitle(
                    View.INVENTORY_COPY_CREATION_VIEW.getTitle()
            );

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.setScene(
                    new Scene(root)
            );

            stage.showAndWait();

            refreshTable();

        } catch (IOException e) {
            logger.error(e);

            new Alert(
                    Alert.AlertType.ERROR,
                    "Unexpected error occurred. Try again."
            ).show();
        }
    }
}