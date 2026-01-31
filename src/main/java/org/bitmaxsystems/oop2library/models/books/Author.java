package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;

@Entity
@Table(name = "authors")

public class Author extends BookParameter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    protected Author (){}

    public Author(String name) {
        this.name = name;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }
}