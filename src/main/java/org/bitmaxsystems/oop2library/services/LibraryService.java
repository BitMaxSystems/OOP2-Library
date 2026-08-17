package org.bitmaxsystems.oop2library.services;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.controllers.AdministrativeBookRegistryController;
import org.bitmaxsystems.oop2library.models.books.*;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class LibraryService {

    private GenericRepository<History> historyRepository = new GenericRepository<>(History.class);
    private static final Logger logger = LogManager.getLogger(LibraryService.class);


    public void lendOutside(User user, Inventory inventory)
    {
        if (user.getLoyaltyPoints()<30)
        {
            throw new IllegalStateException(user.getFirstName()+" "+user.getLastName()+" has insufficient loyalty points " +
                    "to lend "+inventory.getBook().getTitle()+" outside the library!");
        }

        inventory.lendOutside();

        History history = new History.Builder(user, inventory).lendOutside().build();

        historyRepository.save(history);

        logger.info("Inventory book: {} - {} was successfully lent outside for user: {} {}", inventory.getId(), inventory.getBook().getTitle(), user.getFirstName(), user.getLastName());
    }

    public void lendInside(User user, Inventory inventory)
    {

        inventory.lendInside();

        History history = new History.Builder(user, inventory).lendInside().build();

        historyRepository.save(history);

        logger.info("Inventory book: {} - {} was successfully lent inside for user: {} {}", inventory.getId(), inventory.getBook().getTitle(), user.getFirstName(), user.getLastName());

    }


    public void returnBook(History history)
    {
        long timeDiff;
        int calculatedLoyaltyPoints;
        User user = history.getUser();
        Inventory inventory = history.getInventory();

        history.setActualReturnDate(LocalDate.now());
        inventory.returnBook();

        timeDiff = ChronoUnit.DAYS.between(history.getActualReturnDate(),history.getExpectedReturnDate());

        if (timeDiff >= 0)
        {
           calculatedLoyaltyPoints = 2 + (Math.toIntExact(timeDiff) * 2);
        }
        else
        {
            calculatedLoyaltyPoints = 5 - (Math.toIntExact(timeDiff) * 5);
        }

        user.updateLoyaltyPoints(calculatedLoyaltyPoints);

        historyRepository.update(history);

        logger.info("Inventory book: {} - {} was successfully returned by user: {} {}", inventory.getId(), inventory.getBook().getTitle(), user.getFirstName(), user.getLastName());
    }

}