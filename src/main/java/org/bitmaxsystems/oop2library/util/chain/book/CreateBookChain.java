package org.bitmaxsystems.oop2library.util.chain.book;

import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

public class CreateBookChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private final GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    @Override
    public void setNextChain(IBookFormChain chain) {
        this.nextChain = chain;
    }

    @Override
    public void execute(BookDataDTO bookDataDTO) throws Exception {
        Book existingBook = bookGenericRepository.findById(bookDataDTO.getIsbn());

        if (existingBook != null) {
            throw new DataAlreadyExistException(
                    "- Book with ISBN: " + bookDataDTO.getIsbn() + " already exists!"
            );
        }

        Book newBook = new Book(
                bookDataDTO.getIsbn(),
                bookDataDTO.getTitle(),
                bookDataDTO.getAuthor(),
                bookDataDTO.getGenre(),
                bookDataDTO.getPublisher()
        );

        bookGenericRepository.save(newBook);

        Notification notification = new Notification(
                "Book created",
                newBook.getTitle() + " was added to the book registry.",
                NotificationType.BOOK_CREATED,
                NotificationAudience.ADMIN
        );

        notificationRepository.save(notification);

        if (nextChain != null) {
            nextChain.execute(bookDataDTO);
        }
    }
}