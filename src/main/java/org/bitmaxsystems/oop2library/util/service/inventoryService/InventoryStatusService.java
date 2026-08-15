package org.bitmaxsystems.oop2library.util.service.inventoryService;

import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class InventoryStatusService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void lendInside(Inventory inventory) {
        inventory.setStatus(
                inventory.getStatus().lendInside(inventory)
        );

        inventoryRepository.update(inventory);
    }

    public void lendOutside(Inventory inventory) {
        inventory.setStatus(
                inventory.getStatus().lendOutside(inventory)
        );

        inventoryRepository.update(inventory);
    }

    public void returnBook(Inventory inventory) {
        inventory.setStatus(
                inventory.getStatus().returnBook(inventory)
        );

        inventoryRepository.update(inventory);
    }
}