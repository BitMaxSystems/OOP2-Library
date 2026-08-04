package org.bitmaxsystems.oop2library.util.factory;

import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;

public class BookParameterFactory {

        public static BookParameter getBookParameter(BookParameterAbstractFactory factory)
        {
            return factory.createParameter();
        }
}
