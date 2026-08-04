package org.bitmaxsystems.oop2library.models.dto;

import org.bitmaxsystems.oop2library.models.books.Author;
import org.bitmaxsystems.oop2library.models.books.Book;
import org.bitmaxsystems.oop2library.models.books.Genre;
import org.bitmaxsystems.oop2library.models.books.Publisher;

public class BookDataDTO {
    private String isbn;
    private String title;
    private Genre genre;
    private Author author;
    private Publisher publisher;
    private Book book;

    public static class Builder {
        private String isbn;
        private String title;
        private Genre genre;
        private Author author;
        private Publisher publisher;
        private Book book;

        public Builder(String isbn, String title, Genre genre, Author author, Publisher publisher) {
            this.isbn = isbn;
            this.title = title;
            this.genre = genre;
            this.author = author;
            this.publisher = publisher;
            this.book = null;
        }

        public Builder setBook(Book book) {
            this.book = book;
            return this;
        }

        public BookDataDTO build()
        {
            return new BookDataDTO(isbn,title,genre,author,publisher,book);
        }
    }

    private BookDataDTO(String isbn, String title, Genre genre, Author author, Publisher publisher, Book book) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.publisher = publisher;
        this.book = book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public Genre getGenre() {
        return genre;
    }

    public Author getAuthor() {
        return author;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public Book getBook() {
        return book;
    }
}
