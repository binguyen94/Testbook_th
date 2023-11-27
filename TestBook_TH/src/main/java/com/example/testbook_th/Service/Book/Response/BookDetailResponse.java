package com.example.testbook_th.Service.Book.Response;

import com.example.testbook_th.Domain.Enumration.BookType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@Data
@Builder
@AllArgsConstructor
public class BookDetailResponse {
    private Long id;

    private String title;

    private LocalDate publishDate;

    private BigDecimal price;

    private String description;

    private Long categoryId;

    private List<Long> authorIds;

    private BookType type;

}
