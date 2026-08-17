package org.bitmaxsystems.oop2library.repository;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.users.User;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class HistoryRepository {

    private static HistoryRepository repository = null;
    private HistoryRepository() {}

    public static HistoryRepository getInstance()
    {
        if (repository == null)
        {
            repository = new HistoryRepository();
        }

        return repository;
    }

    public List<History> searchByUser(User user)
    {
        try (Session session = HibernateUtil.getSessionFactory().openSession())
        {
            Query<History> query = session.createQuery("FROM History WHERE user = :user", History.class);
            query.setParameter("user",user);
            return query.list();
        }
    }

}
