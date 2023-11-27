package com.example.testbook_th.Repository;

import com.example.testbook_th.Domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
