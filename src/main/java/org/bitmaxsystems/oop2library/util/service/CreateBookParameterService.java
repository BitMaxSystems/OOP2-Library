package org.bitmaxsystems.oop2library.util.service;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class CreateBookParameterService {

    public <T> void create(BookParameterTypeDTO<T> bookParameterTypeDTO) throws DataAlreadyExistException
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            int count;
            Class<T> tClass = bookParameterTypeDTO.gettClass();
            Query<Long> query = session.createQuery("SELECT COUNT(*) FROM " + tClass.getSimpleName() + " WHERE name = :name", Long.class);
            query.setParameter("name", bookParameterTypeDTO.getFactory().getParameter());
            count = Math.toIntExact(query.getResultCount());
            if (count > 0) {
                throw new DataAlreadyExistException("- A book parameter with this value already exists");
            }
            else
            {
                Transaction transaction;
                BookParameter parameter = bookParameterTypeDTO.getFactory().createParameter();
                transaction = session.beginTransaction();
                session.persist(parameter);
                transaction.commit();
            }
        }
    }
}
