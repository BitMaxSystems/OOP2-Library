import org.bitmaxsystems.oop2library.exceptions.ChildRecordExistException;
import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.services.BookService;
import org.bitmaxsystems.oop2library.services.InventoryService;
import org.bitmaxsystems.oop2library.util.chain.book.CreateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.UpdateBookChain;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookCrudTest {

    private GenericRepository<Book> bookRepository;
    private GenericRepository<Author> authorRepository;
    private GenericRepository<Genre> genreRepository;
    private GenericRepository<Publisher> publisherRepository;
    private GenericRepository<Inventory> inventoryRepository;

    private InventoryService inventoryService;
    private BookService bookService;

    private Author author;
    private Genre genre;
    private Publisher publisher;

    private String isbn;

    @BeforeEach
    void setup() {
        bookRepository = new GenericRepository<>(Book.class);
        authorRepository = new GenericRepository<>(Author.class);
        genreRepository = new GenericRepository<>(Genre.class);
        publisherRepository = new GenericRepository<>(Publisher.class);
        inventoryRepository = new GenericRepository<>(Inventory.class);

        inventoryService =
                new InventoryService();

        bookService =
                new BookService();

        String unique =
                String.valueOf(System.nanoTime());

        author = new Author(
                "Book Test Author " + unique
        );

        genre = new Genre(
                "Book Test Genre " + unique
        );

        publisher = new Publisher(
                "Book Test Publisher " + unique
        );

        authorRepository.save(author);
        genreRepository.save(genre);
        publisherRepository.save(publisher);

        long number =
                Math.abs(
                        System.nanoTime()
                                % 1_000_000
                );

        isbn = String.format(
                "978-0-12-%06d-7",
                number
        );
    }

    private BookDataDTO createBookDataDTO() {
        return new BookDataDTO.Builder(
                isbn,
                "Book Test Title",
                genre,
                author,
                publisher
        ).build();
    }

    private Book createPersistedBook()
            throws Exception {

        IBookFormChain createBook =
                new CreateBookChain();

        createBook.execute(
                createBookDataDTO()
        );

        return bookRepository.findById(
                isbn
        );
    }

    @Test
    void testBookCreate() {
        IBookFormChain createBook =
                new CreateBookChain();

        BookDataDTO bookDataDTO =
                createBookDataDTO();

        assertDoesNotThrow(
                () -> createBook.execute(
                        bookDataDTO
                )
        );

        Book createdBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(createdBook);

        assertEquals(
                isbn,
                createdBook.getIsbn()
        );

        assertEquals(
                "Book Test Title",
                createdBook.getTitle()
        );

        assertEquals(
                author.getId(),
                createdBook.getAuthor()
                        .getId()
        );

        assertEquals(
                genre.getId(),
                createdBook.getGenre()
                        .getId()
        );

        assertEquals(
                publisher.getId(),
                createdBook.getPublisher()
                        .getId()
        );
    }

    @Test
    void testBookRead() throws Exception {
        IBookFormChain createBook =
                new CreateBookChain();

        createBook.execute(
                createBookDataDTO()
        );

        Book foundBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(foundBook);

        assertEquals(
                isbn,
                foundBook.getIsbn()
        );

        assertEquals(
                "Book Test Title",
                foundBook.getTitle()
        );

        assertEquals(
                author.getId(),
                foundBook.getAuthor()
                        .getId()
        );

        assertEquals(
                genre.getId(),
                foundBook.getGenre()
                        .getId()
        );

        assertEquals(
                publisher.getId(),
                foundBook.getPublisher()
                        .getId()
        );
    }

    @Test
    void duplicateIsbnExceptionThrown()
            throws Exception {

        IBookFormChain createBook =
                new CreateBookChain();

        BookDataDTO firstBook =
                createBookDataDTO();

        createBook.execute(firstBook);

        BookDataDTO duplicateBook =
                new BookDataDTO.Builder(
                        isbn,
                        "Another Book",
                        genre,
                        author,
                        publisher
                ).build();

        DataAlreadyExistException exception =
                assertThrowsExactly(
                        DataAlreadyExistException.class,
                        () -> createBook.execute(
                                duplicateBook
                        )
                );

        assertEquals(
                "- Book with ISBN: "
                        + isbn
                        + " already exists!",
                exception.getMessage()
        );

        Book persistedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(persistedBook);

        assertEquals(
                "Book Test Title",
                persistedBook.getTitle()
        );
    }

    @Test
    void testBookTitleUpdate()
            throws Exception {

        Book book =
                createPersistedBook();

        BookDataDTO updateData =
                new BookDataDTO.Builder(
                        isbn,
                        "Updated Book Title",
                        genre,
                        author,
                        publisher
                )
                        .setBook(book)
                        .build();

        IBookFormChain updateBook =
                new UpdateBookChain();

        assertDoesNotThrow(
                () -> updateBook.execute(
                        updateData
                )
        );

        Book updatedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(updatedBook);

        assertEquals(
                "Updated Book Title",
                updatedBook.getTitle()
        );
    }

    @Test
    void testBookAuthorUpdate()
            throws Exception {

        Book book =
                createPersistedBook();

        Author newAuthor =
                new Author(
                        "Updated Author "
                                + System.nanoTime()
                );

        authorRepository.save(
                newAuthor
        );

        BookDataDTO updateData =
                new BookDataDTO.Builder(
                        isbn,
                        book.getTitle(),
                        genre,
                        newAuthor,
                        publisher
                )
                        .setBook(book)
                        .build();

        IBookFormChain updateBook =
                new UpdateBookChain();

        assertDoesNotThrow(
                () -> updateBook.execute(
                        updateData
                )
        );

        Book updatedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(updatedBook);

        assertEquals(
                newAuthor.getId(),
                updatedBook.getAuthor()
                        .getId()
        );
    }

    @Test
    void testBookGenreUpdate()
            throws Exception {

        Book book =
                createPersistedBook();

        Genre newGenre =
                new Genre(
                        "Updated Genre "
                                + System.nanoTime()
                );

        genreRepository.save(
                newGenre
        );

        BookDataDTO updateData =
                new BookDataDTO.Builder(
                        isbn,
                        book.getTitle(),
                        newGenre,
                        author,
                        publisher
                )
                        .setBook(book)
                        .build();

        IBookFormChain updateBook =
                new UpdateBookChain();

        assertDoesNotThrow(
                () -> updateBook.execute(
                        updateData
                )
        );

        Book updatedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(updatedBook);

        assertEquals(
                newGenre.getId(),
                updatedBook.getGenre()
                        .getId()
        );
    }

    @Test
    void testBookPublisherUpdate()
            throws Exception {

        Book book =
                createPersistedBook();

        Publisher newPublisher =
                new Publisher(
                        "Updated Publisher "
                                + System.nanoTime()
                );

        publisherRepository.save(
                newPublisher
        );

        BookDataDTO updateData =
                new BookDataDTO.Builder(
                        isbn,
                        book.getTitle(),
                        genre,
                        author,
                        newPublisher
                )
                        .setBook(book)
                        .build();

        IBookFormChain updateBook =
                new UpdateBookChain();

        assertDoesNotThrow(
                () -> updateBook.execute(
                        updateData
                )
        );

        Book updatedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(updatedBook);

        assertEquals(
                newPublisher.getId(),
                updatedBook.getPublisher()
                        .getId()
        );
    }

    @Test
    void testBookDelete() throws Exception {
        Book book =
                createPersistedBook();

        assertNotNull(
                bookRepository.findById(
                        isbn
                )
        );

        assertDoesNotThrow(
                () -> bookService
                        .deleteBook(book)
        );

        assertNull(
                bookRepository.findById(
                        isbn
                )
        );
    }

    @Test
    void deleteBookWithInventoryCopiesExceptionThrown()
            throws Exception {

        Book book =
                createPersistedBook();

        inventoryService
                .createCopies(
                        book,
                        2
                );

        ChildRecordExistException exception =
                assertThrowsExactly(
                        ChildRecordExistException.class,
                        () -> bookService
                                .deleteBook(book)
                );

        assertNotNull(
                exception.getMessage()
        );

        assertTrue(
                exception.getMessage()
                        .contains(
                                "copies of the book"
                        )
        );

        Book persistedBook =
                bookRepository.findById(
                        isbn
                );

        assertNotNull(
                persistedBook
        );

        long numberOfCopies =
                inventoryRepository.findAll()
                        .stream()
                        .filter(copy ->
                                copy.getBook()
                                        .getIsbn()
                                        .equals(isbn)
                        )
                        .count();

        assertEquals(
                2,
                numberOfCopies
        );
    }
}