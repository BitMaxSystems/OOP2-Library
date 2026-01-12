package org.bitmaxsystems.oop2library.service;

import org.bitmaxsystems.oop2library.model.books.*;
import org.bitmaxsystems.oop2library.repository.GenericDao;
import org.bitmaxsystems.oop2library.config.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;

public class LibraryService {

    private final GenericDao<Book> bookDao = new GenericDao<>(Book.class);
    private final GenericDao<Author> authorDao = new GenericDao<>(Author.class);
    private final GenericDao<Genre> genreDao = new GenericDao<>(Genre.class);
    private final GenericDao<Publisher> publisherDao = new GenericDao<>(Publisher.class);
    private final GenericDao<Inventory> inventoryDao = new GenericDao<>(Inventory.class);

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