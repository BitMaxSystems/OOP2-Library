package org.bitmaxsystems.oop2library.util.contracts;

import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;

public interface IBookFormChain {
    void setNextChain (IBookFormChain chain);

    void execute(BookDataDTO bookDataDTO) throws Exception;
}
