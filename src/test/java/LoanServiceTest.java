import org.bitmaxsystems.oop2library.config.HibernateInit;
import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Inventory;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.books.enums.BookStatus;
import org.bitmaxsystems.oop2library.models.dto.LoanDataDTO;
import org.bitmaxsystems.oop2library.models.loans.Loan;
import org.bitmaxsystems.oop2library.models.loans.enums.LoanStatus;
import org.bitmaxsystems.oop2library.models.users.User;
import org.bitmaxsystems.oop2library.models.users.enums.UserRole;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.repository.LoanRepository;
import org.bitmaxsystems.oop2library.service.LoanService;
import org.junit.jupiter.api.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoanServiceTest {

    private static User reader;
    private static Inventory inventory;
    private static final LoanService loanService = new LoanService();
    private static final LoanRepository loanRepository = new LoanRepository();
    private static final GenericRepository<User> userRepository = new GenericRepository<>(User.class);
    private static final GenericRepository<Inventory> inventoryRepository = new GenericRepository<>(Inventory.class);
    private static final GenericRepository<Book> bookRepository = new GenericRepository<>(Book.class);
    private static final GenericRepository<Author> authorRepository = new GenericRepository<>(Author.class);
    private static final GenericRepository<Genre> genreRepository = new GenericRepository<>(Genre.class);
    private static final GenericRepository<Publisher> publisherRepository = new GenericRepository<>(Publisher.class);

    @BeforeAll
    static void setup() {
        HibernateInit.initializeIfEmpty();

        Author author = new Author("Test Author");
        authorRepository.save(author);

        Genre genre = new Genre("Test Genre");
        genreRepository.save(genre);

        Publisher publisher = new Publisher("Test Publisher");
        publisherRepository.save(publisher);

        Book book = new Book("ISBN-TEST-001", "Test Book", author, genre, publisher);
        bookRepository.save(book);

        inventory = new Inventory(book, BookStatus.AVAILABLE);
        inventoryRepository.save(inventory);

        reader = new User.Builder("Loan", "Tester", 25, "+359888111222", UserRole.READER)
                .setDateOfApproval(new Date())
                .build();
        userRepository.save(reader);
    }

    private Date futureDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR, 2);
        return cal.getTime();
    }

    private Date pastDueDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -3);
        return cal.getTime();
    }

    @Test
    @Order(1)
    void testSuccessfulBorrow() {
        LoanDataDTO data = new LoanDataDTO.Builder(reader, inventory, futureDueDate()).build();

        assertDoesNotThrow(() -> loanService.borrowBook(data));

        List<Loan> activeLoans = loanRepository.findActiveByUser(reader);
        assertEquals(1, activeLoans.size());
        assertEquals(LoanStatus.ACTIVE, activeLoans.get(0).getStatus());

        Inventory updatedInventory = inventoryRepository.findById(inventory.getId());
        assertEquals(BookStatus.BORROWED, updatedInventory.getStatus());
    }

    @Test
    @Order(2)
    void testBorrowUnavailableCopyThrows() {
        LoanDataDTO data = new LoanDataDTO.Builder(reader, inventory, futureDueDate()).build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,
                () -> loanService.borrowBook(data));
        assertEquals("This copy is not available for borrowing", exception.getMessage());
    }

    @Test
    @Order(3)
    void testBorrowWithPastDueDateThrows() {
        Author author = new Author("Author2");
        authorRepository.save(author);
        Genre genre = new Genre("Genre2");
        genreRepository.save(genre);
        Publisher publisher = new Publisher("Publisher2");
        publisherRepository.save(publisher);
        Book book2 = new Book("ISBN-TEST-002", "Test Book 2", author, genre, publisher);
        bookRepository.save(book2);
        Inventory inventory2 = new Inventory(book2, BookStatus.AVAILABLE);
        inventoryRepository.save(inventory2);

        LoanDataDTO data = new LoanDataDTO.Builder(reader, inventory2, pastDueDate()).build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,
                () -> loanService.borrowBook(data));
        assertEquals("Due date cannot be in the past", exception.getMessage());
    }

    @Test
    @Order(4)
    void testBorrowAsNonReaderThrows() {
        User admin = new User.Builder("Admin", "Tester", 30, "+359888333444", UserRole.ADMINISTRATOR)
                .setDateOfApproval(new Date())
                .build();
        userRepository.save(admin);

        Author author = new Author("Author3");
        authorRepository.save(author);
        Genre genre = new Genre("Genre3");
        genreRepository.save(genre);
        Publisher publisher = new Publisher("Publisher3");
        publisherRepository.save(publisher);
        Book book3 = new Book("ISBN-TEST-003", "Test Book 3", author, genre, publisher);
        bookRepository.save(book3);
        Inventory inventory3 = new Inventory(book3, BookStatus.AVAILABLE);
        inventoryRepository.save(inventory3);

        LoanDataDTO data = new LoanDataDTO.Builder(admin, inventory3, futureDueDate()).build();

        DataValidationException exception = assertThrowsExactly(DataValidationException.class,
                () -> loanService.borrowBook(data));
        assertEquals("Only approved readers can borrow books", exception.getMessage());
    }

    @Test
    @Order(5)
    void testSuccessfulReturn() {
        Loan activeLoan = loanRepository.findActiveByUser(reader).get(0);

        LoanDataDTO data = new LoanDataDTO.Builder(reader, activeLoan.getInventory(), new Date()).build();
        data.setLoan(activeLoan);

        assertDoesNotThrow(() -> loanService.returnBook(data));

        List<Loan> activeLoans = loanRepository.findActiveByUser(reader);
        assertEquals(0, activeLoans.size());

        Inventory updatedInventory = inventoryRepository.findById(inventory.getId());
        assertEquals(BookStatus.AVAILABLE, updatedInventory.getStatus());
    }

    @Test
    @Order(6)
    void testReturnIncreasesLoyaltyPoints() {
        int pointsBefore = reader.getLoyaltyPoints();

        Author author = new Author("Author4");
        authorRepository.save(author);
        Genre genre = new Genre("Genre4");
        genreRepository.save(genre);
        Publisher publisher = new Publisher("Publisher4");
        publisherRepository.save(publisher);
        Book book4 = new Book("ISBN-TEST-004", "Test Book 4", author, genre, publisher);
        bookRepository.save(book4);
        Inventory inventory4 = new Inventory(book4, BookStatus.AVAILABLE);
        inventoryRepository.save(inventory4);

        LoanDataDTO borrowData = new LoanDataDTO.Builder(reader, inventory4, futureDueDate()).build();
        assertDoesNotThrow(() -> loanService.borrowBook(borrowData));

        Loan loan = loanRepository.findActiveByUser(reader).get(0);
        LoanDataDTO returnData = new LoanDataDTO.Builder(reader, inventory4, new Date()).build();
        returnData.setLoan(loan);
        assertDoesNotThrow(() -> loanService.returnBook(returnData));

        User updatedReader = userRepository.findById(reader.getId());
        assertTrue(updatedReader.getLoyaltyPoints() > pointsBefore);
    }

    @Test
    @Order(7)
    void testMaxLoansCapEnforced() {
        for (int i = 5; i <= 7; i++) {
            Author author = new Author("Author" + i);
            authorRepository.save(author);
            Genre genre = new Genre("Genre" + i);
            genreRepository.save(genre);
            Publisher publisher = new Publisher("Publisher" + i);
            publisherRepository.save(publisher);
            Book book = new Book("ISBN-TEST-00" + i, "Test Book " + i, author, genre, publisher);
            bookRepository.save(book);
            Inventory inv = new Inventory(book, BookStatus.AVAILABLE);
            inventoryRepository.save(inv);

            LoanDataDTO data = new LoanDataDTO.Builder(reader, inv, futureDueDate()).build();

            if (i <= 7) {
                if (loanRepository.countActiveByUser(reader) < 3) {
                    assertDoesNotThrow(() -> loanService.borrowBook(data));
                } else {
                    DataValidationException exception = assertThrowsExactly(DataValidationException.class,
                            () -> loanService.borrowBook(data));
                    assertTrue(exception.getMessage().contains("maximum"));
                }
            }
        }
    }

    @Test
    @Order(8)
    void testMarkOverdueLoans() {
        loanService.markOverdueLoans();

        List<Loan> overdueLoans = loanRepository.findOverdue();
        overdueLoans.forEach(loan ->
                assertEquals(LoanStatus.OVERDUE, loan.getStatus()));
    }
}