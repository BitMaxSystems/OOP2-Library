package org.bitmaxsystems.oop2library.util.chain.book;

import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

public class CreateBookChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);

    @Override
    public void setNextChain(IBookFormChain chain) {
        this.nextChain = chain;
    }

    @Override
    public void execute(BookDataDTO bookDataDTO) throws Exception {
        Book existingBook = bookGenericRepository.findById(bookDataDTO.getIsbn());

        if (existingBook != null)
        {
            throw new DataAlreadyExistException("- Book with ISBN: "+bookDataDTO.getIsbn()+" already exists!");
        }
        else
        {
            Book newBook = new Book(bookDataDTO.getIsbn(),
                    bookDataDTO.getTitle(),
                    bookDataDTO.getAuthor(),
                    bookDataDTO.getGenre(),
                    bookDataDTO.getPublisher());

            bookGenericRepository.save(newBook);

            if (nextChain != null)
            {
                nextChain.execute(bookDataDTO);
            }
        }
    }
}
