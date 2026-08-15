package org.bitmaxsystems.oop2library.config;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.bitmaxsystems.oop2library.util.bookstatus.IBookStatus;
import org.bitmaxsystems.oop2library.util.bookstatus.AvailableBookStatus;
import org.bitmaxsystems.oop2library.util.bookstatus.LentInsideBookStatus;
import org.bitmaxsystems.oop2library.util.bookstatus.LentOutsideBookStatus;


@Converter
public class BookStatusConverter implements AttributeConverter<IBookStatus, String> {

    @Override
    public String convertToDatabaseColumn(IBookStatus status) {
        if (status == null) {
            return null;
        }

        return status.getStatus();
    }

    @Override
    public IBookStatus convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }

        return switch (value) {
            case "AVAILABLE" -> new AvailableBookStatus();
            case "LENT_INSIDE" -> new LentInsideBookStatus();
            case "LENT_OUTSIDE" -> new LentOutsideBookStatus();

            default -> throw new IllegalArgumentException("Unknown book status: " + value);
        };
    }
}
