package org.bitmaxsystems.oop2library.util.bookstatus;

import org.bitmaxsystems.oop2library.models.books.Inventory;

public class LentOutsideBookStatus implements IBookStatus{

    @Override
    public String getStatus(){
        return "LENT_OUTSIDE";
    }

    @Override
    public IBookStatus lendInside(Inventory inventory) {
        throw new IllegalStateException("Book is already lent");
    }

    @Override
    public IBookStatus lendOutside(Inventory inventory) {
        throw new IllegalStateException("Book is already lent");
    }

    @Override
    public IBookStatus returnBook(Inventory inventory) {
        return new AvailableBookStatus();
    }

    @Override
    public String toString() {
        return "Lent outside";
    }
}
