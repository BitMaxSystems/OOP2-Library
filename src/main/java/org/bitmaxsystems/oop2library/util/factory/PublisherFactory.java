package org.bitmaxsystems.oop2library.util.factory;

import org.bitmaxsystems.oop2library.models.books.BookParameter;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;

public class PublisherFactory implements BookParameterAbstractFactory {
    private String parameter;


    @Override
    public String getParameter() {
        return parameter;
    }

    @Override
    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public BookParameter createParameter() {
        return new Publisher(parameter);
    }
}
