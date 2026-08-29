import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.history.History;
import org.bitmaxsystems.oop2library.models.history.LendStatusEnum;
import org.bitmaxsystems.oop2library.models.inventory.Inventory;
import org.bitmaxsystems.oop2library.models.inventory.states.InventoryStateEnum;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.services.LibraryService;
import org.bitmaxsystems.oop2library.services.inventoryService.InventoryService;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

public class LibraryServiceTest {

    private final LibraryService libraryService = new LibraryService();
    private final GenericRepository<User> userRepository = new GenericRepository<>(User.class);
    private final GenericRepository<History> historyRepository = new GenericRepository<>(History.class);
    private final GenericRepository<Inventory> inventoryRepository = new GenericRepository<>(Inventory.class);

    private Inventory availableInventory;
    private Inventory archivedInventory;
    private Inventory lendInsideInventory;
    private Inventory lendOutsideInventory;

    private User normalReader;
    private User lowLoyaltyPointsReader;


    @BeforeEach
    void setup()
    {
        GenericRepository<Author> authorRepository = new GenericRepository<>(Author.class);
        GenericRepository<Genre> genreRepository = new GenericRepository<>(Genre.class);
        GenericRepository<Publisher> publisherRepository = new GenericRepository<>(Publisher.class);
        GenericRepository<Book> bookRepository = new GenericRepository<>(Book.class);

        InventoryService inventoryService = new InventoryService();

        String unique = String.valueOf(System.nanoTime());

        Author author = new Author("Status Test Author " + unique);
        Genre genre = new Genre("Status Test Genre " + unique);
        Publisher publisher = new Publisher("Status Test Publisher " + unique);

        authorRepository.save(author);
        genreRepository.save(genre);
        publisherRepository.save(publisher);

        Book book = new Book(
                "STATUS-" + unique,
                "Status Test Book",
                author,
                genre,
                publisher
        );

        bookRepository.save(book);

        availableInventory = new Inventory(book);
        archivedInventory = new Inventory(book);
        archivedInventory.setArchived(true);

        lendInsideInventory = new Inventory(book);
        lendOutsideInventory = new Inventory(book);

        inventoryRepository.save(availableInventory);
        inventoryRepository.save(archivedInventory);
        inventoryRepository.save(lendInsideInventory);
        inventoryRepository.save(lendOutsideInventory);

        inventoryService.lendInside(lendInsideInventory);
        inventoryService.lendOutside(lendOutsideInventory);

        normalReader = new User.Builder("Test","User",22,"+359888000001", UserRole.READER).build();
        lowLoyaltyPointsReader = new User.Builder("Test","User",22,"+359888000001", UserRole.READER).build();
        lowLoyaltyPointsReader.setLoyaltyPoints(20);

        userRepository.save(normalReader);
        userRepository.save(lowLoyaltyPointsReader);

    }

    @Test
    void testNormalUserLendOutside()
    {
        libraryService.lendOutside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        availableInventory = inventoryRepository.findById(availableInventory.getId());

        assertSame(InventoryStateEnum.LENT_OUTSIDE,availableInventory.getState().getStateEnum(),"The state of the inventory copy must be LEND OUTSIDE");

        assertNotNull(historyRecord,"A history record should be present for the book lending!");

        assertSame(availableInventory.getId(),historyRecord.getInventory().getId(),"The inventory copy in the history record does not match the inventory object");
        assertEquals(normalReader.getId(), historyRecord.getUser().getId(), "The user in the history record does not match the user object");
        assertTrue(LocalDate.now().isEqual(historyRecord.getDateOfLending()),"The date of lending does not match!");
        assertSame(LendStatusEnum.IN_TIME, historyRecord.getLendStatus());
        assertNull(historyRecord.getActualReturnDate(),"The book is already returned!");
        assertTrue(LocalDate.now().plusMonths(1).isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");

    }

    @Test
    void testNormalUserLendInside()
    {
        libraryService.lendInside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        availableInventory = inventoryRepository.findById(availableInventory.getId());

        assertSame(InventoryStateEnum.LENT_INSIDE,availableInventory.getState().getStateEnum(),"The state of the inventory copy must be LEND INSIDE");

        assertNotNull(historyRecord,"A history record should be present for the book lending!");

        assertSame(availableInventory.getId(),historyRecord.getInventory().getId(),"The inventory copy in the history record does not match the inventory object");
        assertEquals(normalReader.getId(), historyRecord.getUser().getId(), "The user in the history record does not match the user object");
        assertTrue(LocalDate.now().isEqual(historyRecord.getDateOfLending()),"The date of lending does not match!");
        assertSame(LendStatusEnum.IN_TIME, historyRecord.getLendStatus());
        assertNull(historyRecord.getActualReturnDate(),"The book is already returned!");
        assertTrue(LocalDate.now().isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");

    }

    @Test
    void testNormalUserReturnLendOutsideCopy()
    {
        libraryService.lendOutside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        libraryService.returnBook(historyRecord,false);

        availableInventory = inventoryRepository.findById(availableInventory.getId());
        normalReader = userRepository.findById(normalReader.getId());

        assertSame(InventoryStateEnum.AVAILABLE,availableInventory.getState().getStateEnum(),"The state of the inventory copy must be AVAILABLE");

        assertNotNull(historyRecord,"A history record should be present for the book lending!");

        assertSame(availableInventory.getId(),historyRecord.getInventory().getId(),"The inventory copy in the history record does not match the inventory object");
        assertEquals(normalReader.getId(), historyRecord.getUser().getId(), "The user in the history record does not match the user object");
        assertTrue(LocalDate.now().isEqual(historyRecord.getDateOfLending()),"The date of lending does not match!");
        assertSame(LendStatusEnum.RETURNED, historyRecord.getLendStatus());
        assertTrue(LocalDate.now().plusMonths(1).isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");
        assertNotNull(historyRecord.getActualReturnDate(),"The book is not returned returned!");
        assertTrue(LocalDate.now().isEqual(historyRecord.getActualReturnDate()),"The return date are not the same!");

        long timeDiff = ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.now().plusMonths(1));
        int calculatedUserLoyaltyPoints = 50 + (2+(Math.toIntExact(timeDiff) * 2));
        if (calculatedUserLoyaltyPoints>100)
        {
            calculatedUserLoyaltyPoints = 100;
        }
        assertEquals(calculatedUserLoyaltyPoints,normalReader.getLoyaltyPoints(),"The loyalty points are not the same!");

    }

    @Test
    void testNormalUserReturnLendInsideCopy()
    {
        libraryService.lendInside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        libraryService.returnBook(historyRecord,false);

        availableInventory = inventoryRepository.findById(availableInventory.getId());
        normalReader = userRepository.findById(normalReader.getId());

        assertSame(InventoryStateEnum.AVAILABLE,availableInventory.getState().getStateEnum(),"The state of the inventory copy must be AVAILABLE");

        assertNotNull(historyRecord,"A history record should be present for the book lending!");

        assertSame(availableInventory.getId(),historyRecord.getInventory().getId(),"The inventory copy in the history record does not match the inventory object");
        assertEquals(normalReader.getId(), historyRecord.getUser().getId(), "The user in the history record does not match the user object");
        assertTrue(LocalDate.now().isEqual(historyRecord.getDateOfLending()),"The date of lending does not match!");
        assertSame(LendStatusEnum.RETURNED, historyRecord.getLendStatus());
        assertTrue(LocalDate.now().isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");
        assertNotNull(historyRecord.getActualReturnDate(),"The book is not returned returned!");
        assertTrue(LocalDate.now().isEqual(historyRecord.getActualReturnDate()),"The return date are not the same!");

        long timeDiff = ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.now());
        int calculatedUserLoyaltyPoints = 50 + (2+(Math.toIntExact(timeDiff) * 2));
        if (calculatedUserLoyaltyPoints>100)
        {
            calculatedUserLoyaltyPoints = 100;
        }

        assertEquals(calculatedUserLoyaltyPoints,normalReader.getLoyaltyPoints(),"The loyalty points are not the same!");

    }

    @Test
    void testNormalUserOverdue()
    {
        libraryService.lendInside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        assertNotNull(historyRecord,"A history record should be present for the book lending!");


//        Simulate Overdue situation
        historyRecord.setExpectedReturnDate(historyRecord.getExpectedReturnDate().minusDays(2));
        historyRepository.update(historyRecord);

        historyRecord = historyRepository.findById(historyRecord.getId());


        assertSame(LendStatusEnum.OVERDUE, historyRecord.getLendStatus());
        assertTrue(LocalDate.now().minusDays(2).isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");
        assertNull(historyRecord.getActualReturnDate(),"The book is already returned!");


    }

    @Test
    void testNormalUserReturnOverdue()
    {
        /*
        * The results are similar, no matter the lending type. Used "LEND INSIDE" for easier simulation of an overdue situation
        * */
        libraryService.lendInside(normalReader,availableInventory);

        History historyRecord;

        try(Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<History> query = session.createQuery("FROM History where user=:user AND inventory=:inventory", History.class);
            query.setMaxResults(1);
            query.setParameter("user",normalReader);
            query.setParameter("inventory",availableInventory);
            historyRecord = query.getSingleResult();
        }

        assertNotNull(historyRecord,"A history record should be present for the book lending!");


//        Simulate Overdue situation
        historyRecord.setExpectedReturnDate(historyRecord.getExpectedReturnDate().minusDays(2));
        historyRepository.update(historyRecord);

        historyRecord = historyRepository.findById(historyRecord.getId());

        libraryService.returnBook(historyRecord,false);

        availableInventory = inventoryRepository.findById(availableInventory.getId());
        normalReader = userRepository.findById(normalReader.getId());

        assertSame(InventoryStateEnum.AVAILABLE,availableInventory.getState().getStateEnum(),"The state of the inventory copy must be AVAILABLE");

        assertSame(availableInventory.getId(),historyRecord.getInventory().getId(),"The inventory copy in the history record does not match the inventory object");
        assertEquals(normalReader.getId(), historyRecord.getUser().getId(), "The user in the history record does not match the user object");
        assertTrue(LocalDate.now().isEqual(historyRecord.getDateOfLending()),"The date of lending does not match!");
        assertSame(LendStatusEnum.RETURNED_OVERDUE, historyRecord.getLendStatus());
        assertTrue(LocalDate.now().minusDays(2).isEqual(historyRecord.getExpectedReturnDate()),"The expected date does not match!");
        assertNotNull(historyRecord.getActualReturnDate(),"The book is not returned returned!");
        assertTrue(LocalDate.now().isEqual(historyRecord.getActualReturnDate()),"The return date are not the same!");

        long timeDiff = ChronoUnit.DAYS.between(LocalDate.now(),LocalDate.now().minusDays(2));
        int calculatedUserLoyaltyPoints = 50 + (Math.toIntExact(timeDiff) * 5);
        if (calculatedUserLoyaltyPoints< 0)
        {
            calculatedUserLoyaltyPoints = 0;
        }

        assertEquals(calculatedUserLoyaltyPoints,normalReader.getLoyaltyPoints(),"The loyalty points are not the same!");


    }

    @Test
    void alreadyLentInsideException()
    {

        IllegalStateException exception =
                assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendInside(normalReader,lendInsideInventory));
                assertEquals("Book is already lent",exception.getMessage());
        exception =  assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendOutside(normalReader,lendInsideInventory));
        assertEquals("Book is already lent",exception.getMessage());



    }

    @Test
    void alreadyLentOutsideException()
    {

        IllegalStateException exception =
                assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendInside(normalReader,lendOutsideInventory));
        assertEquals("Book is already lent",exception.getMessage());
        exception = assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendInside(normalReader,lendOutsideInventory));
        assertEquals("Book is already lent",exception.getMessage());


    }

    @Test
    void alreadyReturnedException()
    {
//        In the Hstory.Builder class, lendOutside just adds the dates, it does not change the state of the inventory object
        History history = new History.Builder(normalReader,availableInventory).lendOutside().build();
        history.setActualReturnDate(LocalDate.now());
        historyRepository.save(history);

        IllegalStateException exception =
                assertThrowsExactly(IllegalStateException.class,() ->libraryService.returnBook(history,false));
        assertEquals("Book is already available",exception.getMessage());


    }

    @Test
    void archivedBooksCannotBeLentOutsideException()
    {
        IllegalStateException exception =
                assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendOutside(normalReader,archivedInventory));
        assertEquals("Archived books cannot be lent outside.",exception.getMessage());
    }

    @Test
    void insufficientLoyaltyPointsException()
    {
        assertThrowsExactly(IllegalStateException.class,() ->libraryService.lendOutside(lowLoyaltyPointsReader,availableInventory));
    }

}



