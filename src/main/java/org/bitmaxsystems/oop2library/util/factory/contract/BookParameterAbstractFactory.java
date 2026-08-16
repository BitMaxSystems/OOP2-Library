package org.bitmaxsystems.oop2library.util.factory.contract;

import org.bitmaxsystems.oop2library.models.books.IBookParameter;

public interface BookParameterAbstractFactory {

    IBookParameter createParameter();
    void setParameter(String parameter);
    String getParameter();
}
