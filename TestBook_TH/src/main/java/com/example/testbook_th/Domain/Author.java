package com.example.testbook_th.Domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "author")
    private List<BookAuthor> bookAuthors;

    public Author(Long id) {
        this.id = id;
    }

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
