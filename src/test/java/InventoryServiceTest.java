import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.service.inventoryService.ArchiveInventoryCopyService;
import org.bitmaxsystems.oop2library.util.service.inventoryService.CreateInventoryCopiesService;
import org.bitmaxsystems.oop2library.util.service.inventoryService.DeleteInventoryCopyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceTest {

    private CreateInventoryCopiesService createInventoryCopiesService;
    private ArchiveInventoryCopyService archiveInventoryCopyService;
    private DeleteInventoryCopyService deleteInventoryCopyService;

    private GenericRepository<Author> authorRepository;
    private GenericRepository<Genre> genreRepository;
    private GenericRepository<Publisher> publisherRepository;
    private GenericRepository<Book> bookRepository;
    private GenericRepository<Inventory> inventoryRepository;

    private Inventory inventory;

    private Author persistedAuthor;
    private Genre persistedGenre;
    private Publisher persistedPublisher;
    private Book persistedBook;

    @BeforeEach
    void setup() {
        createInventoryCopiesService =
                new CreateInventoryCopiesService();

        archiveInventoryCopyService =
                new ArchiveInventoryCopyService();

        deleteInventoryCopyService =
                new DeleteInventoryCopyService();

        authorRepository =
                new GenericRepository<>(Author.class);

        genreRepository =
                new GenericRepository<>(Genre.class);

        publisherRepository =
                new GenericRepository<>(Publisher.class);

        bookRepository =
                new GenericRepository<>(Book.class);

        inventoryRepository =
                new GenericRepository<>(Inventory.class);

        Book book = new Book(
                "9780441172719",
                "Dune",
                new Author("Frank Herbert"),
                new Genre("Science Fiction"),
                new Publisher("Test Publisher")
        );

        inventory = new Inventory(book);
    }

    private void createPersistedTestBook() {
        String unique = String.valueOf(System.nanoTime());

        persistedAuthor =
                new Author(
                        "Inventory Test Author " + unique
                );

        persistedGenre =
                new Genre(
                        "Inventory Test Genre " + unique
                );

        persistedPublisher =
                new Publisher(
                        "Inventory Test Publisher " + unique
                );

        authorRepository.save(persistedAuthor);
        genreRepository.save(persistedGenre);
        publisherRepository.save(persistedPublisher);

        persistedBook = new Book(
                "TEST-" + unique,
                "Inventory Test Book",
                persistedAuthor,
                persistedGenre,
                persistedPublisher
        );

        bookRepository.save(persistedBook);
    }

    @Test
    void createCopiesNullBookException() {
        IllegalArgumentException exception =
                assertThrowsExactly(
                        IllegalArgumentException.class,
                        () -> createInventoryCopiesService
                                .createCopies(null, 1)
                );

        assertEquals(
                "Book cannot be null.",
                exception.getMessage()
        );
    }

    @Test
    void createCopiesZeroQuantityException() {
        IllegalArgumentException exception =
                assertThrowsExactly(
                        IllegalArgumentException.class,
                        () -> createInventoryCopiesService
                                .createCopies(
                                        inventory.getBook(),
                                        0
                                )
                );

        assertEquals(
                "Quantity must be greater than 0.",
                exception.getMessage()
        );
    }

    @Test
    void createCopiesNegativeQuantityException() {
        IllegalArgumentException exception =
                assertThrowsExactly(
                        IllegalArgumentException.class,
                        () -> createInventoryCopiesService
                                .createCopies(
                                        inventory.getBook(),
                                        -5
                                )
                );

        assertEquals(
                "Quantity must be greater than 0.",
                exception.getMessage()
        );
    }

    @Test
    void archiveNullInventoryException() {
        IllegalArgumentException exception =
                assertThrowsExactly(
                        IllegalArgumentException.class,
                        () -> archiveInventoryCopyService
                                .archive(null)
                );

        assertEquals(
                "Inventory copy cannot be null.",
                exception.getMessage()
        );
    }

    @Test
    void lentInsideInventoryCannotBeArchived() {
        inventory.lendInside();

        IllegalStateException exception =
                assertThrowsExactly(
                        IllegalStateException.class,
                        () -> archiveInventoryCopyService
                                .archive(inventory)
                );

        assertEquals(
                "Only available books can be archived.",
                exception.getMessage()
        );

        assertFalse(inventory.isArchived());
    }

    @Test
    void lentOutsideInventoryCannotBeArchived() {
        inventory.lendOutside();

        IllegalStateException exception =
                assertThrowsExactly(
                        IllegalStateException.class,
                        () -> archiveInventoryCopyService
                                .archive(inventory)
                );

        assertEquals(
                "Only available books can be archived.",
                exception.getMessage()
        );

        assertFalse(inventory.isArchived());
    }

    @Test
    void alreadyArchivedInventoryCannotBeArchivedAgain() {
        inventory.setArchived(true);

        IllegalStateException exception =
                assertThrowsExactly(
                        IllegalStateException.class,
                        () -> archiveInventoryCopyService
                                .archive(inventory)
                );

        assertEquals(
                "Book is already archived.",
                exception.getMessage()
        );

        assertTrue(inventory.isArchived());
    }

    @Test
    void deleteNullInventoryException() {
        IllegalArgumentException exception =
                assertThrowsExactly(
                        IllegalArgumentException.class,
                        () -> deleteInventoryCopyService
                                .delete(null)
                );

        assertEquals(
                "Inventory copy cannot be null.",
                exception.getMessage()
        );
    }

    @Test
    void createOneInventoryCopy() {
        createPersistedTestBook();

        assertDoesNotThrow(
                () -> createInventoryCopiesService
                        .createCopies(
                                persistedBook,
                                1
                        )
        );

        long numberOfCopies =
                inventoryRepository.findAll()
                        .stream()
                        .filter(copy ->
                                copy.getBook()
                                        .getIsbn()
                                        .equals(
                                                persistedBook.getIsbn()
                                        )
                        )
                        .count();

        assertEquals(
                1,
                numberOfCopies
        );
    }

    @Test
    void createMultipleInventoryCopies() {
        createPersistedTestBook();

        assertDoesNotThrow(
                () -> createInventoryCopiesService
                        .createCopies(
                                persistedBook,
                                3
                        )
        );

        var copies =
                inventoryRepository.findAll()
                        .stream()
                        .filter(copy ->
                                copy.getBook()
                                        .getIsbn()
                                        .equals(
                                                persistedBook.getIsbn()
                                        )
                        )
                        .toList();

        assertEquals(
                3,
                copies.size()
        );

        for (Inventory copy : copies) {
            assertInstanceOf(
                    AvailableInventoryState.class,
                    copy.getStatus()
            );

            assertFalse(
                    copy.isArchived()
            );
        }
    }

    @Test
    void archiveAvailableInventoryCopy() {
        createPersistedTestBook();

        createInventoryCopiesService.createCopies(
                persistedBook,
                1
        );

        Inventory copy =
                inventoryRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getBook()
                                        .getIsbn()
                                        .equals(
                                                persistedBook.getIsbn()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        assertFalse(
                copy.isArchived()
        );

        assertDoesNotThrow(
                () -> archiveInventoryCopyService
                        .archive(copy)
        );

        Inventory persistedCopy =
                inventoryRepository.findById(
                        copy.getId()
                );

        assertNotNull(
                persistedCopy
        );

        assertTrue(
                persistedCopy.isArchived()
        );

        assertInstanceOf(
                AvailableInventoryState.class,
                persistedCopy.getStatus()
        );
    }

    @Test
    void deleteInventoryCopy() {
        createPersistedTestBook();

        createInventoryCopiesService.createCopies(
                persistedBook,
                1
        );

        Inventory copy =
                inventoryRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getBook()
                                        .getIsbn()
                                        .equals(
                                                persistedBook.getIsbn()
                                        )
                        )
                        .findFirst()
                        .orElseThrow();

        int inventoryId =
                copy.getId();

        assertNotNull(
                inventoryRepository.findById(
                        inventoryId
                )
        );

        assertDoesNotThrow(
                () -> deleteInventoryCopyService
                        .delete(copy)
        );

        assertNull(
                inventoryRepository.findById(
                        inventoryId
                )
        );
    }
}