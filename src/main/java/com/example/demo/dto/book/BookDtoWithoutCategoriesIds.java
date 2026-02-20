package com.example.demo.dto.book;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class BookDtoWithoutCategoriesIds {
    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private String isbn;
    private String description;
    private String coverImage;
}
