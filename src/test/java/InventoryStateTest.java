import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.inventory.states.LentInsideInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentOutsideInventoryState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryStateTest {

    private Inventory inventory;

    @BeforeEach
    void setup() {
        Book book = new Book(
                "9780441172719",
                "Dune",
                new Author("Frank Herbert"),
                new Genre("Science Fiction"),
                new Publisher("Test Publisher")
        );

        inventory = new Inventory(book);
    }

    @Test
    void testDefaultInventoryState() {
        assertInstanceOf(AvailableInventoryState.class, inventory.getStatus());
        assertEquals(
                InventoryStateEnum.AVAILABLE,
                inventory.getStatus().getStatusEnum()
        );
        assertFalse(inventory.isArchived());
    }

    @Test
    void testLendInside() {
        assertDoesNotThrow(inventory::lendInside);

        assertInstanceOf(
                LentInsideInventoryState.class,
                inventory.getStatus()
        );

        assertEquals(
                InventoryStateEnum.LENT_INSIDE,
                inventory.getStatus().getStatusEnum()
        );
    }

    @Test
    void testLendOutside() {
        assertDoesNotThrow(inventory::lendOutside);

        assertInstanceOf(
                LentOutsideInventoryState.class,
                inventory.getStatus()
        );

        assertEquals(
                InventoryStateEnum.LENT_OUTSIDE,
                inventory.getStatus().getStatusEnum()
        );
    }

    @Test
    void availableBookReturnException() {
        assertThrowsExactly(
                IllegalStateException.class,
                inventory::returnBook
        );
    }

    @Test
    void lentInsideBookCanBeReturned() {
        inventory.lendInside();

        assertDoesNotThrow(inventory::returnBook);

        assertInstanceOf(
                AvailableInventoryState.class,
                inventory.getStatus()
        );
    }

    @Test
    void lentOutsideBookCanBeReturned() {
        inventory.lendOutside();

        assertDoesNotThrow(inventory::returnBook);

        assertInstanceOf(
                AvailableInventoryState.class,
                inventory.getStatus()
        );
    }

    @Test
    void lentInsideBookCannotBeLentInsideAgain() {
        inventory.lendInside();

        assertThrowsExactly(
                IllegalStateException.class,
                inventory::lendInside
        );
    }

    @Test
    void lentInsideBookCannotBeLentOutside() {
        inventory.lendInside();

        assertThrowsExactly(
                IllegalStateException.class,
                inventory::lendOutside
        );
    }

    @Test
    void lentOutsideBookCannotBeLentInside() {
        inventory.lendOutside();

        assertThrowsExactly(
                IllegalStateException.class,
                inventory::lendInside
        );
    }

    @Test
    void lentOutsideBookCannotBeLentOutsideAgain() {
        inventory.lendOutside();

        assertThrowsExactly(
                IllegalStateException.class,
                inventory::lendOutside
        );
    }

    @Test
    void archivedBookCanBeLentInside() {
        inventory.setArchived(true);

        assertDoesNotThrow(inventory::lendInside);

        assertInstanceOf(
                LentInsideInventoryState.class,
                inventory.getStatus()
        );

        assertTrue(inventory.isArchived());
    }

    @Test
    void archivedBookCannotBeLentOutside() {
        inventory.setArchived(true);

        assertThrowsExactly(
                IllegalStateException.class,
                inventory::lendOutside
        );

        assertInstanceOf(
                AvailableInventoryState.class,
                inventory.getStatus()
        );
    }

    @Test
    void archivedBookRemainsArchivedAfterReturn() {
        inventory.setArchived(true);

        inventory.lendInside();
        inventory.returnBook();

        assertTrue(inventory.isArchived());

        assertInstanceOf(
                AvailableInventoryState.class,
                inventory.getStatus()
        );
    }

    @Test
    void testAvailableStatusValues() {
        AvailableInventoryState state =
                new AvailableInventoryState();

        assertEquals(
                InventoryStateEnum.AVAILABLE,
                state.getStatusEnum()
        );

        assertEquals("Available", state.toString());
    }

    @Test
    void testLentInsideStatusValues() {
        LentInsideInventoryState state =
                new LentInsideInventoryState();

        assertEquals(
                InventoryStateEnum.LENT_INSIDE,
                state.getStatusEnum()
        );

        assertEquals("Lent inside", state.toString());
    }

    @Test
    void testLentOutsideStatusValues() {
        LentOutsideInventoryState state =
                new LentOutsideInventoryState();

        assertEquals(
                InventoryStateEnum.LENT_OUTSIDE,
                state.getStatusEnum()
        );

        assertEquals("Lent outside", state.toString());
    }
}