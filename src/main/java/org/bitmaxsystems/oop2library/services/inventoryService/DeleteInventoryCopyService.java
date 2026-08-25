package org.bitmaxsystems.oop2library.services.inventoryService;

import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

public class DeleteInventoryCopyService {

    private final GenericRepository<Inventory> inventoryRepository =
            new GenericRepository<>(Inventory.class);

    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

    public void delete(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory copy cannot be null.");
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
                NotificationAudience.ADMIN
        );

        notificationRepository.save(notification);
    }
}