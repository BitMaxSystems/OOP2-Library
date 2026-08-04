package org.bitmaxsystems.oop2library.util.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteBookService {
    private static final Logger logger = LogManager.getLogger(DeleteBookService.class);
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);


    public void deleteBook(Book book)
    {
//        Update the code to check inventory
        bookGenericRepository.delete(book);
    }

}
