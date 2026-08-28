import org.bitmaxsystems.oop2library.exceptions.DataValidationException;
import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;
import org.bitmaxsystems.oop2library.models.dto.BookDataDTO;
import org.bitmaxsystems.oop2library.util.chain.book.contract.IBookFormChain;
import org.bitmaxsystems.oop2library.util.chain.book.VerifyBookDataChain;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookDataValidationTest {

    @Test
    void isbnExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "9780441172719",
                "Dune",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- ISBN is not in the correct format.\n",
                exception.getMessage()
        );
    }

    @Test
    void emptyTitleExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- Title cannot be empty.\n",
                exception.getMessage()
        );
    }

    @Test
    void whitespaceTitleExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "   ",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- Title cannot be empty.\n",
                exception.getMessage()
        );
    }

    @Test
    void genreNotSelectedExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "Dune",
                null,
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- Genre not selected.\n",
                exception.getMessage()
        );
    }

    @Test
    void authorNotSelectedExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "Dune",
                new Genre("Science Fiction"),
                null,
                new Publisher("Test Publisher")
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- Author not selected.\n",
                exception.getMessage()
        );
    }

    @Test
    void publisherNotSelectedExceptionThrown() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "Dune",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                null
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        assertEquals(
                "- Publisher not selected.\n",
                exception.getMessage()
        );
    }

    @Test
    void multipleInvalidFieldsExceptionThrown() {
        List<String> errorList;

        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "invalid-isbn",
                "",
                null,
                null,
                null
        ).build();

        DataValidationException exception = assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(bookDataDTO)
        );

        errorList = Arrays.stream(
                exception.getMessage().split("\n")
        ).toList();

        assertEquals(
                5,
                errorList.size()
        );
    }

    @Test
    void testValidBookData() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO bookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "Dune",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        assertDoesNotThrow(
                () -> verifyData.execute(bookDataDTO)
        );
    }

    @Test
    void validatorCanBeReusedAfterInvalidData() {
        IBookFormChain verifyData = new VerifyBookDataChain();

        BookDataDTO invalidBookDataDTO = new BookDataDTO.Builder(
                "",
                "",
                null,
                null,
                null
        ).build();

        assertThrowsExactly(
                DataValidationException.class,
                () -> verifyData.execute(invalidBookDataDTO)
        );

        BookDataDTO validBookDataDTO = new BookDataDTO.Builder(
                "978-0-44-117271-9",
                "Dune",
                new Genre("Science Fiction"),
                new Author("Frank Herbert"),
                new Publisher("Test Publisher")
        ).build();

        assertDoesNotThrow(
                () -> verifyData.execute(validBookDataDTO)
        );
    }
}