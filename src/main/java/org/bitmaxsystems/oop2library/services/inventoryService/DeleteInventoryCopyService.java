package org.bitmaxsystems.oop2library.services.inventoryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.controllers.AdministrativeInventoryController;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.repository.GenericRepository;

public class DeleteInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    private static final Logger logger =
            LogManager.getLogger(DeleteInventoryCopyService.class);

    public void delete(Inventory inventory) {

       try {


           if (inventory == null) {
               throw new IllegalArgumentException("Inventory copy cannot be null.");
           }

           if (inventory.getState().getStateEnum() != InventoryStateEnum.AVAILABLE) {
               throw new IllegalArgumentException("Inventory must be available before deletion");

           }

           inventoryRepository.delete(inventory);
           logger.info("Inventory copy successfully deleted");
       } catch (Exception e) {
           logger.error(e);
           throw e;
       }
    }
}