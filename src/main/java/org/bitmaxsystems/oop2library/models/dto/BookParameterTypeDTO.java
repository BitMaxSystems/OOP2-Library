package org.bitmaxsystems.oop2library.models.dto;

import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;

public class BookParameterTypeDTO<T> {
    private Class<T> tClass;
    private BookParameterAbstractFactory factory;

    public BookParameterTypeDTO(Class<T> tClass, BookParameterAbstractFactory factory) {
        this.tClass = tClass;
        this.factory = factory;
    }

    public Class<T> gettClass() {
        return tClass;
    }

    public BookParameterAbstractFactory getFactory() {
        return factory;
    }
}
