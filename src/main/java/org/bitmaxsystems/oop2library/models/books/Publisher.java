package org.bitmaxsystems.oop2library.models.books;

import jakarta.persistence.*;

@Entity
@Table(name = "publishers")
public class Publisher extends BookParameter{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    protected Publisher() {}
    public Publisher(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String toString() {
        return name;
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