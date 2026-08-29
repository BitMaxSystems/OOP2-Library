package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.repository.BookParameterRepository;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

public class BookParameterService {

    private BookParameterRepository repository = BookParameterRepository.getInstance();
    private static final Logger logger = LogManager.getLogger(BookParameterService.class);


    public <T> List<T> findAllParameterRecords(Class<T> tClass)
    {
        return repository.findAllParameterRecords(tClass);
    }
    public <T> void create(BookParameterTypeDTO<T> bookParameterTypeDTO) throws DataAlreadyExistException
    {
        try {
            int count;
            Class<T> tClass = bookParameterTypeDTO.gettClass();
            count = repository.checkIfParameterExists(tClass.getSimpleName(), bookParameterTypeDTO.getFactory().getParameter());
            if (count > 0) {
                throw new DataAlreadyExistException("- A book parameter with this value already exists");
            } else {
                IBookParameter parameter = bookParameterTypeDTO.getFactory().createParameter();
                repository.saveParameter(parameter);
                logger.info("{} parameter successfully created", bookParameterTypeDTO.gettClass().getSimpleName());
            }
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }

    }


    public void update(IBookParameter parameter)
    {
        try {
            long countBooksWithParameter = repository.checkCountOfBookWithParameter(parameter);
            if (countBooksWithParameter> 0)
            {
                throw new ChildRecordExistException("Books with this "+parameter.getClass().getSimpleName()+" exist!");

            }
            repository.updateParameter(parameter);
            logger.info("{} successfully updated!",parameter.getClass().getSimpleName());

        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public <T> void delete(BookParameterTypeDTO<T> bookParameterTypeDTO)
    {
        try {
            IBookParameter parameter = bookParameterTypeDTO.getParameter();
            long countBooksWithParameter = repository.checkCountOfBookWithParameter(parameter);
                if (countBooksWithParameter> 0)
                {
                    throw new ChildRecordExistException("Books with this "+parameter.getClass().getSimpleName()+" exist!");

                }
                repository.deleteParameter(parameter);
                logger.info("{} successfully deleted!",bookParameterTypeDTO.gettClass().getSimpleName());

//
        }
//        catch (ConstraintViolationException e) {
//                throw new ChildRecordExistException("Books with this "+bookParameterTypeDTO.gettClass().getSimpleName()+" exist!");
//            }
            catch (Exception e) {
            logger.error(e);
            throw e;
    }

    }
}
