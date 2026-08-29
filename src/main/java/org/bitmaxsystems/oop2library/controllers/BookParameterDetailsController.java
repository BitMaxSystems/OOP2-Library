package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.services.BookParameterService;


public class BookParameterDetailsController<T> {
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
        parameterField.setText(bookParameterTypeDTO.getParameter().getName());
    }

    @FXML
    public void onUpdate()
    {
        IBookParameter parameter = bookParameterTypeDTO.getParameter();
        parameter.setName(parameterField.getText());
        try {
            bookParameterService.update(parameter);
            new Alert(Alert.AlertType.INFORMATION,
                    bookParameterTypeDTO.gettClass().getSimpleName()+" successfully updated!").show();
        } catch (ChildRecordExistException e) {
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onDelete()
    {
        BookParameterService bookParameterService = new BookParameterService();

        try {
            bookParameterService.delete(bookParameterTypeDTO);
            new Alert(Alert.AlertType.INFORMATION,
                    bookParameterTypeDTO.gettClass().getSimpleName()+" successfully deleted!").show();
            onClose();
        }
        catch (ChildRecordExistException e)
        {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        catch (Exception e) {
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) parameterLabel.getScene().getWindow()).close();
    }
}
