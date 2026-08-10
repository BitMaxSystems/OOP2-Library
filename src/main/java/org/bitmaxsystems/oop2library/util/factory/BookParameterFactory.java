package org.bitmaxsystems.oop2library.util.factory;

import org.bitmaxsystems.oop2library.models.books.IBookParameter;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;

public class BookParameterFactory {

        public static IBookParameter getBookParameter(BookParameterAbstractFactory factory)
        {
            return factory.createParameter();
        }
}
