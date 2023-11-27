package com.example.testbook_th.Repository;
import com.example.testbook_th.Domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
