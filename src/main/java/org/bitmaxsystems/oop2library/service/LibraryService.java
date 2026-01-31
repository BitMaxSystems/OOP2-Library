package org.bitmaxsystems.oop2library.service;

import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.bitmaxsystems.oop2library.models.books.*;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class LibraryService {

    private final GenericRepository<Book> bookGenericRepository = new GenericRepository<>(Book.class);
    private final GenericRepository<Author> authorGenericRepository = new GenericRepository<>(Author.class);
    private final GenericRepository<Genre> genreGenericRepository = new GenericRepository<>(Genre.class);
    private final GenericRepository<Publisher> publisherGenericRepository = new GenericRepository<>(Publisher.class);
    private final GenericRepository<Inventory> inventoryGenericRepository = new GenericRepository<>(Inventory.class);

    public boolean registerBook(String title, String isbn, String authorName, String genreName, String publisherName) {
        if (bookExists(isbn)) {
            System.out.println("⚠️ Грешка: Книга с ISBN " + isbn + " вече съществува!");
            return false;
        }

        Author author = getOrCreateAuthor(authorName);
        Genre genre = getOrCreateGenre(genreName);
        Publisher publisher = getOrCreatePublisher(publisherName);

        Book newBook = new Book(isbn, title, author, genre, publisher);
        bookGenericRepository.save(newBook);

        Inventory inv = new Inventory(newBook);
        inventoryGenericRepository.save(inv);

        System.out.println("✅ Успешно регистрирана книга: " + title);
        return true;
    }

    public List<Book> getAllBooks() {
        return bookGenericRepository.findAll();
    }

    public void deleteBook(Book book) {

        bookGenericRepository.delete(book);
        System.out.println("Книгата е изтрита: " + book.getTitle());
    }


    // Помощни функции
    private Author getOrCreateAuthor(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Author> query = session.createQuery("FROM Author WHERE name = :name", Author.class);
            query.setParameter("name", name);
            Author existing = query.uniqueResult();
            if (existing != null) return existing;
        }
        Author newAuthor = new Author(name);
        authorGenericRepository.save(newAuthor);
        return newAuthor;
    }

    private Genre getOrCreateGenre(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Genre> query = session.createQuery("FROM Genre WHERE name = :name", Genre.class);
            query.setParameter("name", name);
            Genre existing = query.uniqueResult();
            if (existing != null) return existing;
        }
        Genre newGenre = new Genre(name);
        genreGenericRepository.save(newGenre);
        return newGenre;
    }

    private Publisher getOrCreatePublisher(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Publisher> query = session.createQuery("FROM Publisher WHERE name = :name", Publisher.class);
            query.setParameter("name", name);
            Publisher existing = query.uniqueResult();
            if (existing != null) return existing;
        }
        Publisher newPublisher = new Publisher(name);
        publisherGenericRepository.save(newPublisher);
        return newPublisher;
    }

    private boolean bookExists(String isbn) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("SELECT count(b) FROM Book b WHERE b.isbn = :isbn", Long.class);
            query.setParameter("isbn", isbn);
            return query.uniqueResult() > 0;
        }
    }
}