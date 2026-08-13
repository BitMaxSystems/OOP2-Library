package org.bitmaxsystems.oop2library.util.service.inventoryService;

import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void delete(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory copy cannot be null.");
        }

        inventoryRepository.delete(inventory);
    }
}