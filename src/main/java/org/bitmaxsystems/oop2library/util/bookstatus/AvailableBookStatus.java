package org.bitmaxsystems.oop2library.util.bookstatus;

import org.bitmaxsystems.oop2library.models.books.Inventory;

public class AvailableBookStatus implements IBookStatus {


    @Override
    public String getStatus() {
        return "AVAILABLE";
    }

    @Override
    public IBookStatus lendInside(Inventory inventory) {
        return new LentInsideBookStatus();
    }

    @Override
    public IBookStatus lendOutside(Inventory inventory) {
        if (inventory.isArchived()) {
            throw new IllegalStateException("Archived books cannot be lent outside.");
        }

        return new LentOutsideBookStatus();
    }

    @Override
    public IBookStatus returnBook(Inventory inventory) {
        throw new IllegalStateException("Book is already available");
    }

    @Override
    public String toString() {
        return "Available";
    }
}
