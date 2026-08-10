package org.bitmaxsystems.oop2library.util.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteBookService {
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);


    public void deleteBook(Book book) throws ChildRecordExistException
    {
        try
        {
            bookGenericRepository.delete(book);

        } catch (Exception e) {
            throw new ChildRecordExistException("One or more books are present in the inventory!");
        }
    }

}
