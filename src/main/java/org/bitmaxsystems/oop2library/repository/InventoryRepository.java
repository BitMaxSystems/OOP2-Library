package org.bitmaxsystems.oop2library.repository;


import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

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

    private IInventoryState convertEnumToState(InventoryStateEnum stateEnum)
    {
        return switch (stateEnum) {
            case AVAILABLE -> new AvailableInventoryState();
            case LENT_INSIDE -> new LentInsideInventoryState();
            case LENT_OUTSIDE -> new LentOutsideInventoryState();
        };
    }

    public List<Inventory> getInventoryByState(InventoryStateEnum stateEnum)
    {
        try (Session session =
                     HibernateUtil.getSessionFactory().openSession()) {

            Query<Inventory> query = session.createQuery(
                    "FROM Inventory i WHERE i.state = :state",
                    Inventory.class
            );

            query.setParameter("state", convertEnumToState(stateEnum));

            return query.list();
        }
    }
}
