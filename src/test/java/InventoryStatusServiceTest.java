import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentInsideInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentOutsideInventoryState;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.service.inventoryService.InventoryStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryStatusServiceTest {

    private GenericRepository<Author> authorRepository;
    private GenericRepository<Genre> genreRepository;
    private GenericRepository<Publisher> publisherRepository;
    private GenericRepository<Book> bookRepository;
    private GenericRepository<Inventory> inventoryRepository;

    private InventoryStatusService inventoryStatusService;
    private Inventory inventory;

    @BeforeEach
    void setup() {
        authorRepository = new GenericRepository<>(Author.class);
        genreRepository = new GenericRepository<>(Genre.class);
        publisherRepository = new GenericRepository<>(Publisher.class);
        bookRepository = new GenericRepository<>(Book.class);
        inventoryRepository = new GenericRepository<>(Inventory.class);

        inventoryStatusService = new InventoryStatusService();

        String unique = String.valueOf(System.nanoTime());

        Author author = new Author("Status Test Author " + unique);
        Genre genre = new Genre("Status Test Genre " + unique);
        Publisher publisher = new Publisher("Status Test Publisher " + unique);

        authorRepository.save(author);
        genreRepository.save(genre);
        publisherRepository.save(publisher);

        Book book = new Book(
                "STATUS-" + unique,
                "Status Test Book",
                author,
                genre,
                publisher
        );

        bookRepository.save(book);

        inventory = new Inventory(book);
        inventoryRepository.save(inventory);
    }

    @Test
    void testLendInsidePersistence() {
        assertDoesNotThrow(() -> inventoryStatusService.lendInside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentInsideInventoryState.class, persistedInventory.getStatus());
    }

    @Test
    void testLendOutsidePersistence() {
        assertDoesNotThrow(() -> inventoryStatusService.lendOutside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentOutsideInventoryState.class, persistedInventory.getStatus());
    }

    @Test
    void testReturnFromLentInsidePersistence() {
        inventoryStatusService.lendInside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertInstanceOf(LentInsideInventoryState.class, lentInventory.getStatus());

        assertDoesNotThrow(() -> inventoryStatusService.returnBook(lentInventory));

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(returnedInventory);
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getStatus());
    }

    @Test
    void testReturnFromLentOutsidePersistence() {
        inventoryStatusService.lendOutside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertInstanceOf(LentOutsideInventoryState.class, lentInventory.getStatus());

        assertDoesNotThrow(() -> inventoryStatusService.returnBook(lentInventory));

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(returnedInventory);
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getStatus());
    }

    @Test
    void archivedBookCanBeLentInsideAndRemainsArchived() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        assertDoesNotThrow(() -> inventoryStatusService.lendInside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentInsideInventoryState.class, persistedInventory.getStatus());
        assertTrue(persistedInventory.isArchived());
    }

    @Test
    void archivedBookCannotBeLentOutside() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        assertThrowsExactly(
                IllegalStateException.class,
                () -> inventoryStatusService.lendOutside(inventory)
        );

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(AvailableInventoryState.class, persistedInventory.getStatus());
        assertTrue(persistedInventory.isArchived());
    }

    @Test
    void archivedBookRemainsArchivedAfterReturn() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        inventoryStatusService.lendInside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertTrue(lentInventory.isArchived());
        assertInstanceOf(LentInsideInventoryState.class, lentInventory.getStatus());

        inventoryStatusService.returnBook(lentInventory);

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertTrue(returnedInventory.isArchived());
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getStatus());
    }

    @Test
    void availableBookCannotBeReturned() {
        assertThrowsExactly(
                IllegalStateException.class,
                () -> inventoryStatusService.returnBook(inventory)
        );

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(AvailableInventoryState.class, persistedInventory.getStatus());
    }
}