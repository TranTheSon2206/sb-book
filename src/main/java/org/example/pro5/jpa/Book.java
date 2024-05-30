package org.example.pro5.jpa;

import jakarta.persistence.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="name")
    private String name;

    @ManyToMany
    @JoinTable(name = "book_publisher",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "publisher_id"))
    private Set<Publisher> publishers;

    // Constructors
    public Book() {
    }

    public Book(String name, Publisher... publishers) {
        this.name = name;
        this.publishers = Stream.of(publishers).collect(Collectors.toSet());
        this.publishers.forEach(publisher -> publisher.getBooks().add(this));
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Publisher> getPublishers() {
        return publishers;
    }

    public void setPublishers(Set<Publisher> publishers) {
        this.publishers = publishers;
    }
}
