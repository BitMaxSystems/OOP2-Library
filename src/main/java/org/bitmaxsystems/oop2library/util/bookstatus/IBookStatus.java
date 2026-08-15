package org.bitmaxsystems.oop2library.util.bookstatus;

import org.bitmaxsystems.oop2library.models.books.Inventory;

public interface IBookStatus {

    String getStatus();

    IBookStatus lendInside(Inventory inventory);

    IBookStatus lendOutside(Inventory inventory);

    IBookStatus returnBook(Inventory inventory);
}