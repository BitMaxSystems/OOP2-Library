package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.view.View;

import java.io.IOException;

public class BookParameterManagementController<T> {
    @FXML
    private TableView<T> tableView;
    @FXML
    private TableColumn<T,String> parameterValueColumn;
    @FXML
    private Button createParameterButton;
    private GenericRepository<T> genericRepository;
    private static final Logger logger = LogManager.getLogger(BookParameterManagementController.class);
    private BookParameterTypeDTO<T> bookParameterTypeDTO;

    public void setBookParameter(BookParameterTypeDTO<T> bookParameterTypeDTO)
    {
        this.bookParameterTypeDTO = bookParameterTypeDTO;
        genericRepository = new GenericRepository<>(bookParameterTypeDTO.gettClass());
        parameterValueColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        createParameterButton.setText("Create "+bookParameterTypeDTO.gettClass().getSimpleName());
        parameterValueColumn.setText(bookParameterTypeDTO.gettClass().getSimpleName());
        refreshData();
    }

    private void refreshData()
    {
        try
        {
            tableView.getItems().setAll(genericRepository.findAll());
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again.").show();
        }
    }

    @FXML
    public void onViewCreate()
    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(View.BOOK_PARAMETER_CREATION_VIEW.getPath()));
            AnchorPane root = loader.load();

            BookParameterCreationController<T> controller = loader.getController();
            controller.setBookParameter(bookParameterTypeDTO);

            Stage stage = new Stage();
            stage.setTitle(bookParameterTypeDTO.gettClass().getSimpleName()+" creation");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
            refreshData();

        } catch (IOException e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error, try again!").show();
        }
    }
}
