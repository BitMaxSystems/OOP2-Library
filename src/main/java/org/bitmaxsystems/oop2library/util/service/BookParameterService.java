package org.bitmaxsystems.oop2library.util.service;

import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.repository.BookParameterRepository;

public class BookParameterService {

    private BookParameterRepository repository = BookParameterRepository.getInstance();
    public <T> void create(BookParameterTypeDTO<T> bookParameterTypeDTO) throws DataAlreadyExistException
    {
            int count;
            Class<T> tClass = bookParameterTypeDTO.gettClass();
            count = repository.checkIfParameterExists(tClass.getSimpleName(), bookParameterTypeDTO.getFactory().getParameter());
            if (count > 0) {
                throw new DataAlreadyExistException("- A book parameter with this value already exists");
            }
            else
            {
                IBookParameter parameter = bookParameterTypeDTO.getFactory().createParameter();
                repository.saveParameter(parameter);
            }

    }
    public void update(IBookParameter parameter)
    {
        repository.updateParameter(parameter);

    }

    public <T> void delete(BookParameterTypeDTO<T> bookParameterTypeDTO) throws DataAlreadyExistException
    {

//            int count;
            IBookParameter parameter = bookParameterTypeDTO.getParameter();
//            count = repository.checkIfBooksWithParametersExist(bookParameterTypeDTO.gettClass().getSimpleName(),parameter);
//
//            if (count>0)
//            {
//                throw new DataAlreadyExistException("Books with this "+bookParameterTypeDTO.gettClass().getSimpleName()+" exist!");
//            }
//            else
//            {
//                repository.deleteParameter(parameter);
//            }
            try {
                repository.deleteParameter(parameter);
            } catch (Exception e) {
                throw new ChildRecordExistException("Books with this "+bookParameterTypeDTO.gettClass().getSimpleName()+" exist!");
            }

    }
}
