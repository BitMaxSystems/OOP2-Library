package org.bitmaxsystems.oop2library.util.factory.contract;

import org.bitmaxsystems.oop2library.models.books.BookParameter;

public interface BookParameterAbstractFactory {

    BookParameter createParameter();
    void setParameter(String parameter);
    String getParameter();
}
