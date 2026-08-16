package org.bitmaxsystems.oop2library.util.service.bookService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.InventoryRepository;

public class DeleteBookService {

    private static final Logger logger =
            LogManager.getLogger(DeleteBookService.class);

    private final GenericRepository<Book> bookGenericRepository =
            new GenericRepository<>(Book.class);

    private final InventoryRepository inventoryRepository = InventoryRepository.getInstance();

    public void deleteBook(Book book) {

        try {
            long inventoryCopies = inventoryRepository.checkNumberOfBooksInInventory(book);

            if (inventoryCopies > 0) {
                throw new ChildRecordExistException(inventoryCopies+" copies of the book "+book.getTitle()+" still exist in the inventory");
            }

            bookGenericRepository.delete(book);

        } catch (Exception e) {
            logger.error("Failed to delete book", e);

            throw e;
        }
    }
}