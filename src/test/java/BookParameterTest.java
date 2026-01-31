import org.bitmaxsystems.oop2library.exceptions.DataAlreadyExistException;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookParameterTypeDTO;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.util.factory.AuthorFactory;
import org.bitmaxsystems.oop2library.util.factory.GenreFactory;
import org.bitmaxsystems.oop2library.util.factory.PublisherFactory;
import org.bitmaxsystems.oop2library.util.factory.contract.BookParameterAbstractFactory;
import org.bitmaxsystems.oop2library.util.service.CreateBookParameterService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookParameterTest {

    private <T> void testBookParameterCreation(Class<T> type, String value, BookParameterAbstractFactory factory) {
        GenericRepository<T> genericRepository = new GenericRepository<>(type);
        int before = genericRepository.findAll().size();
        CreateBookParameterService bookParameterService = new CreateBookParameterService();
        factory.setParameter(value);
        BookParameterTypeDTO<T> bookParameterTypeDTO = new BookParameterTypeDTO<>(type, factory);

        assertDoesNotThrow(() -> bookParameterService.create(bookParameterTypeDTO));

        int after = genericRepository.findAll().size();

        assertNotEquals(before, after);
    }

    @Test
    void testGenreCreate() {
        testBookParameterCreation(Genre.class, "Thriller", new GenreFactory());
    }

    @Test
    void testAuthorCreate() {
        testBookParameterCreation(Author.class, "John Doe", new AuthorFactory());
    }

    @Test
    void testPublisher() {
        testBookParameterCreation(Publisher.class, "Publisher1", new PublisherFactory());
    }

    @Test
    void genreAlreadyExistsException()
    {
        testBookParameterCreation(Genre.class, "Horror", new GenreFactory());

        CreateBookParameterService bookParameterService = new CreateBookParameterService();
        BookParameterAbstractFactory factory = new GenreFactory();
        factory.setParameter("Horror");
        BookParameterTypeDTO<Genre> bookParameterTypeDTO = new BookParameterTypeDTO<>(Genre.class, factory);
        assertThrowsExactly(DataAlreadyExistException.class,() -> bookParameterService.create(bookParameterTypeDTO));
    }

    @Test
    void authorAlreadyExistsException()
    {
        testBookParameterCreation(Author.class, "Jane Doe", new AuthorFactory());

        CreateBookParameterService bookParameterService = new CreateBookParameterService();
        BookParameterAbstractFactory factory = new AuthorFactory();
        factory.setParameter("Jane Doe");
        BookParameterTypeDTO<Author> bookParameterTypeDTO = new BookParameterTypeDTO<>(Author.class, factory);
        assertThrowsExactly(DataAlreadyExistException.class,() -> bookParameterService.create(bookParameterTypeDTO));
    }

    @Test
    void publisherAlreadyExistsException()
    {
        testBookParameterCreation(Publisher.class, "Publisher2", new PublisherFactory());

        CreateBookParameterService bookParameterService = new CreateBookParameterService();
        BookParameterAbstractFactory factory = new PublisherFactory();
        factory.setParameter("Publisher2");
        BookParameterTypeDTO<Publisher> bookParameterTypeDTO = new BookParameterTypeDTO<>(Publisher.class, factory);
        assertThrowsExactly(DataAlreadyExistException.class,() -> bookParameterService.create(bookParameterTypeDTO));
    }
}
