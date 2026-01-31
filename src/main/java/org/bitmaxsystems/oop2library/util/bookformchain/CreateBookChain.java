package org.bitmaxsystems.oop2library.util.bookformchain;

import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.contracts.IBookFormChain;

public class CreateBookChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);

    public void setNextChain(IBookFormChain nextChain) {
        this.nextChain = nextChain;
    }

    @Override
    public void execute(BookDataDTO formData) throws Exception {
        Book existingBook = bookGenericRepository.findById(formData.getIsbn());

        if (existingBook != null)
        {
            throw new DataAlreadyExistException("- Book with ISBN: "+formData.getIsbn()+" already exists!");
        }
        else
        {
            Book newBook = new Book(formData.getIsbn(),
                    formData.getTitle(),
                    formData.getAuthor(),
                    formData.getGenre(),
                    formData.getPublisher());

            bookGenericRepository.save(newBook);

            if (nextChain != null)
            {
                nextChain.execute(formData);
            }
        }
    }
}
