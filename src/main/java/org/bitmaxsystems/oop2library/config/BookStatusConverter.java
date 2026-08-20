package org.bitmaxsystems.oop2library.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bitmaxsystems.oop2library.models.inventory.states.*;


@Converter(autoApply = true)
public class BookStatusConverter implements AttributeConverter<IInventoryState, InventoryStateEnum> {

    @Override
    public InventoryStateEnum convertToDatabaseColumn(IInventoryState state) {
        return state.getStateEnum();
    }

    @Override
    public IInventoryState convertToEntityAttribute(InventoryStateEnum value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }

        return switch (value) {
            case AVAILABLE -> new AvailableInventoryState();
            case LENT_INSIDE -> new LentInsideInventoryState();
            case LENT_OUTSIDE -> new LentOutsideInventoryState();
        };
    }
}
