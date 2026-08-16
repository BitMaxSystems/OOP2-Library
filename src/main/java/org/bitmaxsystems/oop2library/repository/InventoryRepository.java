package org.bitmaxsystems.oop2library.repository;


import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class InventoryRepository {
    private static InventoryRepository repository;

    private InventoryRepository()
    {}

    public static InventoryRepository getInstance()
    {
        if (repository == null)
        {
           repository =  new InventoryRepository();
        }

        return repository;
    }

    public long checkNumberOfBooksInInventory(Book book) {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Query<Long> query = session.createQuery(
                    "SELECT COUNT(i) FROM Inventory i WHERE i.book = :book",
                    Long.class
            );

            query.setParameter("book", book);

            return query.getSingleResult();
        }
    }
}
