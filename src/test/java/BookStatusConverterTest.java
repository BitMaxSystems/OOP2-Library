import org.bitmaxsystems.oop2library.config.BookStatusConverter;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.inventory.states.LentInsideInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.LentOutsideInventoryState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookStatusConverterTest {

    private final BookStatusConverter converter =
            new BookStatusConverter();

    @Test
    void testAvailableStateToDatabase() {
        InventoryStateEnum result =
                converter.convertToDatabaseColumn(
                        new AvailableInventoryState()
                );

        assertEquals(
                InventoryStateEnum.AVAILABLE,
                result
        );
    }

    @Test
    void testLentInsideStateToDatabase() {
        InventoryStateEnum result =
                converter.convertToDatabaseColumn(
                        new LentInsideInventoryState()
                );

        assertEquals(
                InventoryStateEnum.LENT_INSIDE,
                result
        );
    }

    @Test
    void testLentOutsideStateToDatabase() {
        InventoryStateEnum result =
                converter.convertToDatabaseColumn(
                        new LentOutsideInventoryState()
                );

        assertEquals(
                InventoryStateEnum.LENT_OUTSIDE,
                result
        );
    }

    @Test
    void testAvailableDatabaseValueToState() {
        var result =
                converter.convertToEntityAttribute(
                        InventoryStateEnum.AVAILABLE
                );

        assertInstanceOf(
                AvailableInventoryState.class,
                result
        );
    }

    @Test
    void testLentInsideDatabaseValueToState() {
        var result =
                converter.convertToEntityAttribute(
                        InventoryStateEnum.LENT_INSIDE
                );

        assertInstanceOf(
                LentInsideInventoryState.class,
                result
        );
    }

    @Test
    void testLentOutsideDatabaseValueToState() {
        var result =
                converter.convertToEntityAttribute(
                        InventoryStateEnum.LENT_OUTSIDE
                );

        assertInstanceOf(
                LentOutsideInventoryState.class,
                result
        );
    }
}