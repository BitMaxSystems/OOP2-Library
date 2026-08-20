package org.bitmaxsystems.oop2library.services.inventoryService;

import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateInventoryCopiesService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void createCopies(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null.");
        }

        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0.");
        }

        List<Inventory> copies = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            copies.add(new Inventory(book));
        }

        inventoryRepository.saveAll(copies);
    }
}
