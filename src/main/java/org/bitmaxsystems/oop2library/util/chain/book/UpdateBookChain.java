package org.bitmaxsystems.oop2library.util.chain.book;

import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

public class UpdateBookChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);

    @Override
    public void setNextChain(IBookFormChain chain) {
        this.nextChain = chain;
    }

    @Override
    public void execute(BookDataDTO bookDataDTO) throws Exception {

        Book selectedBook = bookDataDTO.getBook();

        selectedBook.setTitle(bookDataDTO.getTitle());
        selectedBook.setAuthor(bookDataDTO.getAuthor());
        selectedBook.setGenre(bookDataDTO.getGenre());
        selectedBook.setPublisher(bookDataDTO.getPublisher());


        bookGenericRepository.update(selectedBook);

        if (nextChain != null)
        {
            nextChain.execute(bookDataDTO);
        }
    }
}
