package org.bitmaxsystems.oop2library.util.chain.book;

import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.InventoryRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;

public class UpdateBookChain implements IBookFormChain {
    private IBookFormChain nextChain;
    private final GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();
    private final InventoryRepository inventoryRepository = InventoryRepository.getInstance();

    @Override
    public void setNextChain(IBookFormChain chain) {
        this.nextChain = chain;
    }

    @Override
    public void execute(BookDataDTO bookDataDTO) throws Exception {
        Book selectedBook = bookDataDTO.getBook();

        long existingInventory = inventoryRepository.checkNumberOfBooksInInventory(selectedBook);

        if (existingInventory > 0)
        {
            throw new ChildRecordExistException("Cannot update book when inventory copies exist!");
        }

        selectedBook.setTitle(bookDataDTO.getTitle());
        selectedBook.setAuthor(bookDataDTO.getAuthor());
        selectedBook.setGenre(bookDataDTO.getGenre());
        selectedBook.setPublisher(bookDataDTO.getPublisher());

        bookGenericRepository.update(selectedBook);

        Notification notification = new Notification(
                "Book updated",
                selectedBook.getTitle() + " was updated in the book registry.",
                NotificationType.BOOK_UPDATED,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);

        if (nextChain != null) {
            nextChain.execute(bookDataDTO);
        }
    }
}