package org.bitmaxsystems.oop2library.services;

import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.hibernate.exception.ConstraintViolationException;

public class DeleteBookService {
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);


    public void deleteBook(Book book) throws ChildRecordExistException
    {
        try
        {
            bookGenericRepository.delete(book);

        } catch (ConstraintViolationException e) {
            throw new ChildRecordExistException("One or more books are present in the inventory!");
        }
    }

}
