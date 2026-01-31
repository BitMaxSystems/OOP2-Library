package org.bitmaxsystems.oop2library.util.service;

import org.bitmaxsystems.oop2library.config.HibernateUtil;

import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class DeleteBookParameterService {

    public <T> void delete(BookParameterTypeDTO<T> bookParameterTypeDTO) throws DataAlreadyExistException
    {
        try(Session session = HibernateUtil.getSessionFactory().openSession())
        {
            int count;
            BookParameter parameter = bookParameterTypeDTO.getParameter();
            Query<Long> query = session.createQuery(
                    "SELECT COUNT(*) FROM "+ Book.class.getSimpleName() +" where "+bookParameterTypeDTO.gettClass().getSimpleName()+"=:id"
                    ,Long.class);

            query.setParameter("id",parameter);
            count = Math.toIntExact(query.getSingleResult());

            if (count>0)
            {
                throw new DataAlreadyExistException("Books with this "+bookParameterTypeDTO.gettClass().getSimpleName()+" exist!");
            }
            else
            {
                Transaction transaction;
                transaction = session.beginTransaction();
                session.remove(parameter);
                transaction.commit();
            }
        }
    }
}
