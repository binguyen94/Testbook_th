package com.example.testbook_th.Domain;
import com.example.testbook_th.Domain.Enumration.BookType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "books")
@Data
@NoArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private LocalDate publishDate;

    private String description;

    private BigDecimal price;

    @Enumerated(value = EnumType.STRING)
    private BookType type;

    @OneToMany(mappedBy = "book")
    private List<BookAuthor> bookAuthors;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
