package com.example.testbook_th.Repository;

import com.example.testbook_th.Domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {

    @Query(value = "SELECT b FROM Book b " +
            "WHERE " +
            "b.title LIKE :search OR " +
            "b.description LIKE :search OR " +
            "EXISTS (SELECT 1 FROM BookAuthor ba WHERE ba.book = b AND ba.author.name LIKE :search) OR " +
            "b.category.name LIKE :search")
    Page<Book> searchBooks(String search, Pageable pageable);

}
