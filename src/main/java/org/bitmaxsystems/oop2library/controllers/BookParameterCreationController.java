package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.services.BookParameterService;


public class BookParameterCreationController<T> {
    @FXML
    private TextField parameterField;
    @FXML
    private Label parameterLabel;
    private BookParameterTypeDTO<T> bookParameterTypeDTO;
    private BookParameterService bookParameterService = new BookParameterService();

    public void setBookParameter(BookParameterTypeDTO<T> bookParameterTypeDTO)
    {
        this.bookParameterTypeDTO = bookParameterTypeDTO;
        parameterLabel.setText(bookParameterTypeDTO.gettClass().getSimpleName()+":");
    }

    @FXML
    public void onCreate()
    {
        bookParameterTypeDTO.getFactory().setParameter(parameterField.getText());
        try {
            bookParameterService.create(bookParameterTypeDTO);
            new Alert(Alert.AlertType.INFORMATION
                    ,bookParameterTypeDTO.gettClass().getSimpleName()+" parameter successfully created").show();
            onClose();
        }
        catch (DataAlreadyExistException e)
        {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (Exception e)
        {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) parameterLabel.getScene().getWindow()).close();
    }
}
