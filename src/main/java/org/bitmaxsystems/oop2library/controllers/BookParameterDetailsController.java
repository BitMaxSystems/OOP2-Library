package org.bitmaxsystems.oop2library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.util.service.DeleteBookParameterService;
import org.bitmaxsystems.oop2library.util.service.DeleteUserService;
import org.bitmaxsystems.oop2library.util.service.UpdateBookParameterService;


public class BookParameterDetailsController<T> {
    @FXML
    private TextField parameterField;
    @FXML
    private Label parameterLabel;
    private BookParameterTypeDTO<T> bookParameterTypeDTO;
    private static final Logger logger = LogManager.getLogger(BookParameterDetailsController.class);

    public void setBookParameter(BookParameterTypeDTO<T> bookParameterTypeDTO)
    {
        this.bookParameterTypeDTO = bookParameterTypeDTO;
        parameterLabel.setText(bookParameterTypeDTO.gettClass().getSimpleName()+":");
        parameterField.setText(bookParameterTypeDTO.getParameter().getName());
    }

    @FXML
    public void onUpdate()
    {
        UpdateBookParameterService updateBookParameterService = new UpdateBookParameterService();
        BookParameter parameter = bookParameterTypeDTO.getParameter();
        parameter.setName(parameterField.getText());
        try {
            updateBookParameterService.update(parameter);
            logger.info("{} successfully updated!",bookParameterTypeDTO.gettClass().getSimpleName());
            new Alert(Alert.AlertType.INFORMATION,
                    bookParameterTypeDTO.gettClass().getSimpleName()+" successfully updated!").show();
        } catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onDelete()
    {
        DeleteBookParameterService deleteBookParameterService = new DeleteBookParameterService();

        try {
            deleteBookParameterService.delete(bookParameterTypeDTO);
            logger.info("{} successfully deleted!",bookParameterTypeDTO.gettClass().getSimpleName());
            new Alert(Alert.AlertType.INFORMATION,
                    bookParameterTypeDTO.gettClass().getSimpleName()+" successfully deleted!").show();
        }
        catch (DataAlreadyExistException e)
        {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            onClose();
        }
        catch (Exception e) {
            logger.error(e);
            new Alert(Alert.AlertType.ERROR,"Unexpected error occurred. Try again").show();
        }
    }

    @FXML
    public void onClose()
    {
        ((Stage) parameterLabel.getScene().getWindow()).close();
    }
}
