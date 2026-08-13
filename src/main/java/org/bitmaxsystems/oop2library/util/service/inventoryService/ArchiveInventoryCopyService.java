package org.bitmaxsystems.oop2library.util.service.inventoryService;

import org.bitmaxsystems.oop2library.models.books.BookStatus;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class ArchiveInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void archive(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory copy cannot be null.");
        }

        inventory.setStatus(BookStatus.ARCHIVED);
        inventoryRepository.update(inventory);
    }
}
