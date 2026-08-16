package org.bitmaxsystems.oop2library.util.service.inventoryService;

import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;

public class ArchiveInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    public void archive(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory copy cannot be null.");
        }

        if (!(inventory.getStatus() instanceof AvailableInventoryState)) {
            throw new IllegalStateException("Only available books can be archived.");
        }

        if (inventory.isArchived()) {
            throw new IllegalStateException(
                    "Book is already archived."
            );
        }

        inventory.setArchived(true);
        inventoryRepository.update(inventory);
    }
}
