import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentInsideInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentOutsideInventoryState;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.services.inventoryService.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryStatusServiceTest {

    private GenericRepository<Author> authorRepository;
    private GenericRepository<Genre> genreRepository;
    private GenericRepository<Publisher> publisherRepository;
    private GenericRepository<Book> bookRepository;
    private GenericRepository<Inventory> inventoryRepository;

    private InventoryService inventoryService;
    private Inventory inventory;

    @BeforeEach
    void setup() {
        authorRepository = new GenericRepository<>(Author.class);
        genreRepository = new GenericRepository<>(Genre.class);
        publisherRepository = new GenericRepository<>(Publisher.class);
        bookRepository = new GenericRepository<>(Book.class);
        inventoryRepository = new GenericRepository<>(Inventory.class);

        inventoryService = new InventoryService();

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
        assertDoesNotThrow(() -> inventoryService.lendInside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentInsideInventoryState.class, persistedInventory.getState());
    }

    @Test
    void testLendOutsidePersistence() {
        assertDoesNotThrow(() -> inventoryService.lendOutside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentOutsideInventoryState.class, persistedInventory.getState());
    }

    @Test
    void testReturnFromLentInsidePersistence() {
        inventoryService.lendInside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertInstanceOf(LentInsideInventoryState.class, lentInventory.getState());

        assertDoesNotThrow(() -> inventoryService.returnBook(lentInventory));

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(returnedInventory);
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getState());
    }

    @Test
    void testReturnFromLentOutsidePersistence() {
        inventoryService.lendOutside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertInstanceOf(LentOutsideInventoryState.class, lentInventory.getState());

        assertDoesNotThrow(() -> inventoryService.returnBook(lentInventory));

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(returnedInventory);
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getState());
    }

    @Test
    void archivedBookCanBeLentInsideAndRemainsArchived() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        assertDoesNotThrow(() -> inventoryService.lendInside(inventory));

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(LentInsideInventoryState.class, persistedInventory.getState());
        assertTrue(persistedInventory.isArchived());
    }

    @Test
    void archivedBookCannotBeLentOutside() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        assertThrowsExactly(
                IllegalStateException.class,
                () -> inventoryService.lendOutside(inventory)
        );

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(AvailableInventoryState.class, persistedInventory.getState());
        assertTrue(persistedInventory.isArchived());
    }

    @Test
    void archivedBookRemainsArchivedAfterReturn() {
        inventory.setArchived(true);
        inventoryRepository.update(inventory);

        inventoryService.lendInside(inventory);

        Inventory lentInventory = inventoryRepository.findById(inventory.getId());

        assertTrue(lentInventory.isArchived());
        assertInstanceOf(LentInsideInventoryState.class, lentInventory.getState());

        inventoryService.returnBook(lentInventory);

        Inventory returnedInventory = inventoryRepository.findById(inventory.getId());

        assertTrue(returnedInventory.isArchived());
        assertInstanceOf(AvailableInventoryState.class, returnedInventory.getState());
    }

    @Test
    void availableBookCannotBeReturned() {
        assertThrowsExactly(
                IllegalStateException.class,
                () -> inventoryService.returnBook(inventory)
        );

        Inventory persistedInventory = inventoryRepository.findById(inventory.getId());

        assertNotNull(persistedInventory);
        assertInstanceOf(AvailableInventoryState.class, persistedInventory.getState());
    }
}