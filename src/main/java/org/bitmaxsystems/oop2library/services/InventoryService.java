package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.AvailableInventoryState;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.notifications.Notification;
import org.bitmaxsystems.oop2library.models.notifications.NotificationAudience;
import org.bitmaxsystems.oop2library.models.notifications.NotificationType;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.InventoryRepository;
import org.bitmaxsystems.oop2library.repository.NotificationRepository;

import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    private final InventoryRepository inventoryRepository =
            InventoryRepository.getInstance();

    private final GenericRepository<Inventory> inventoryGenericRepository = new GenericRepository<>(Inventory.class);
    private final NotificationRepository notificationRepository = NotificationRepository.getInstance();

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

            inventoryGenericRepository.delete(inventory);

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

        inventoryGenericRepository.saveAll(copies);

        Notification notification = new Notification(
                "Inventory copies added",
                quantity + " copies of " + book.getTitle()
                        + " were added to the inventory.",
                NotificationType.INVENTORY_UPDATED,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);
    }

    public void archive(Inventory inventory) {
        if (inventory == null) {
            throw new IllegalArgumentException("Inventory copy cannot be null.");
        }

        if (!(inventory.getState() instanceof AvailableInventoryState)) {
            throw new IllegalStateException("Only available books can be archived.");
        }

        if (inventory.isArchived()) {
            throw new IllegalStateException("Book is already archived.");
        }

        inventory.setArchived(true);
        inventory.setNeedsArchiving(false);

        inventoryGenericRepository.update(inventory);

        Notification notification = new Notification(
                "Inventory copy archived",
                "Inventory copy #" + inventory.getId()
                        + " of " + inventory.getBook().getTitle()
                        + " was archived.",
                NotificationType.INVENTORY_UPDATED,
                NotificationAudience.STAFF
        );

        notificationRepository.save(notification);
    }
}