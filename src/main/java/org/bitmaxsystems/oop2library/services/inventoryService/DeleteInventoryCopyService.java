package org.bitmaxsystems.oop2library.services.inventoryService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.controllers.AdministrativeInventoryController;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

public class DeleteInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();
    private static final Logger logger =
            LogManager.getLogger(DeleteInventoryCopyService.class);

    public void delete(Inventory inventory) {

      try {
              if (inventory == null) {
                  throw new IllegalArgumentException(
                          "Inventory copy cannot be null."
                  );
              }

              if (inventory.getState().getStateEnum()
                      != InventoryStateEnum.AVAILABLE) {
                  throw new IllegalArgumentException(
                          "Inventory must be available before deletion"
                  );
              }

              int inventoryId = inventory.getId();
              String bookTitle = inventory.getBook().getTitle();

              inventoryRepository.delete(inventory);

              Notification notification = new Notification(
                      "Inventory copy deleted",
                      "Inventory copy #" + inventoryId
                              + " of " + bookTitle
                              + " was deleted.",
                      NotificationType.INVENTORY_UPDATED,
                      NotificationAudience.STAFF
              );

              notificationRepository.save(notification);

              logger.info(
                      "Inventory copy {} of book '{}' successfully deleted",
                      inventoryId,
                      bookTitle
              );
          } catch (Exception e) {
              logger.error(e);
              throw e;
    }
}
}