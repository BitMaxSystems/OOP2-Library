package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.chain.book.CreateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.UpdateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.VerifyBookDataChain;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

import java.util.List;

public class BookService {
    private static final Logger logger = LogManager.getLogger(BookService.class);
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);



    public List<Book> getAllBooks()
    {
        return bookGenericRepository.findAll();
    }

    public void createBook(BookDataDTO bookDataDTO) throws Exception
    {
        IBookFormChain verifyData = new VerifyBookDataChain();
        IBookFormChain createBook = new CreateBookChain();

        verifyData.setNextChain(createBook);

        try {
            verifyData.execute(bookDataDTO);
            logger.info("Book is created!");
        }
        catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }
    public void updateBook(BookDataDTO bookDataDTO) throws Exception
    {
        IBookFormChain verifyData = new VerifyBookDataChain();
        IBookFormChain updateBook = new UpdateBookChain();

        verifyData.setNextChain(updateBook);
        try {
            verifyData.execute(bookDataDTO);
            logger.info("Book is updated!");
        }
         catch (Exception e) {
            logger.error(e);
            throw e;
        }

    }
}
