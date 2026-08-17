package org.bitmaxsystems.oop2library.services.inventoryService;

import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class InventoryStatusService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void lendInside(Inventory inventory) {
        inventory.lendInside();

        inventoryRepository.update(inventory);
    }

    public void lendOutside(Inventory inventory) {
        inventory.lendOutside();

        inventoryRepository.update(inventory);
    }

    public void returnBook(Inventory inventory) {
        inventory.returnBook();

        inventoryRepository.update(inventory);
    }
}