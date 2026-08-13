package org.bitmaxsystems.oop2library.util.service.bookService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class DeleteBookService {

    private static final Logger logger =
            LogManager.getLogger(DeleteBookService.class);

    private final GenericRepository<Book> bookGenericRepository =
            new GenericRepository<>(Book.class);

    public boolean deleteBook(Book book) {
        if (book == null) {
            return false;
        }

        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Inventory i WHERE i.book = :book",
                    Long.class
            );

            query.setParameter("book", book);

            long inventoryCopies = query.getSingleResult();

            if (inventoryCopies > 0) {
                return false;
            }

            bookGenericRepository.delete(book);
            return true;

        } catch (Exception e) {
            logger.error("Failed to delete book", e);
            return false;
        }
    }
}