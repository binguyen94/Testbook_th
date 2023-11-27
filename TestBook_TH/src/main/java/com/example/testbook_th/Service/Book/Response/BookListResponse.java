package com.example.testbook_th.Service.Book.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookListResponse {
    private Long id;

    private String title;

    private LocalDate publishDate;

    private BigDecimal price;

    private String description;

    private String category;

    private String bookAuthor;

    private String type;
}
