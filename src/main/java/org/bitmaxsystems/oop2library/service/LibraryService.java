package org.bitmaxsystems.oop2library.service;

import org.bitmaxsystems.oop2library.models.books.*;
import org.bitmaxsystems.oop2library.repository.GenericRepository;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class LibraryService {

    private final GenericRepository<Book> bookDao = new GenericRepository<>(Book.class);
    private final GenericRepository<Author> authorDao = new GenericRepository<>(Author.class);
    private final GenericRepository<Genre> genreDao = new GenericRepository<>(Genre.class);
    private final GenericRepository<Publisher> publisherDao = new GenericRepository<>(Publisher.class);
    private final GenericRepository<Inventory> inventoryDao = new GenericRepository<>(Inventory.class);

    public boolean registerBook(String title, String isbn, String authorName, String genreName, String publisherName) {
        if (bookExists(isbn)) {
            System.out.println("⚠️ Грешка: Книга с ISBN " + isbn + " вече съществува!");
            return false;
        }

        Author author = getOrCreateAuthor(authorName);
        Genre genre = getOrCreateGenre(genreName);
        Publisher publisher = getOrCreatePublisher(publisherName);

        Book newBook = new Book(isbn, title, author, genre, publisher);
        bookDao.save(newBook);

        Inventory inv = new Inventory(newBook, BookStatus.AVAILABLE);
        inventoryDao.save(inv);

        System.out.println("✅ Успешно регистрирана книга: " + title);
        return true;
    }

    public List<Book> getAllBooks() {
        return bookDao.findAll();
    }

    public void deleteBook(Book book) {

        bookDao.delete(book);
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
        authorDao.save(newAuthor);
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
        genreDao.save(newGenre);
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
        publisherDao.save(newPublisher);
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