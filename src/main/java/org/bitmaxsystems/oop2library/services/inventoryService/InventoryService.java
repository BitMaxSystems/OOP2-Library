package org.bitmaxsystems.oop2library.services.inventoryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.controllers.LibraryHistoryController;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.InventoryRepository;

import java.util.List;

public class InventoryService {

    private final InventoryRepository inventoryRepository =
            InventoryRepository.getInstance();

    private final GenericRepository<Inventory> inventoryGenericRepository = new GenericRepository<>(Inventory.class);
    private static final Logger logger = LogManager.getLogger(InventoryService.class);


    public List<Inventory> getAvailableBooks()
    {
        try {
           List<Inventory> inventoryList = inventoryRepository.getInventoryByState(InventoryStateEnum.AVAILABLE);
           logger.info("Loaded all available inventory");
           return inventoryList;
        } catch (Exception e) {
            logger.error(e);
            throw e;
        }
    }

    public List<Inventory> getAllInventoryCopies()
    {
        return inventoryGenericRepository.findAll();
    }

    public void lendInside(Inventory inventory) {
        inventory.lendInside();

        inventoryGenericRepository.update(inventory);
    }

    public void lendOutside(Inventory inventory) {
        inventory.lendOutside();

        inventoryGenericRepository.update(inventory);
    }

    public void returnBook(Inventory inventory) {
        inventory.returnBook();

        inventoryGenericRepository.update(inventory);
    }
}