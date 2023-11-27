package com.example.testbook_th.Repository;


import com.example.testbook_th.Domain.BookAuthor;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {
    @Transactional
    void deleteBookAuthorsByBookId(Long id);
}
