package com.example.demo.repository.book.spec;

import static com.example.demo.constant.ApplicationConstant.AUTHOR_KEY;

import com.example.demo.model.Book;
import com.example.demo.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return AUTHOR_KEY;
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root.get(AUTHOR_KEY)
                .in(Arrays.stream(params).toArray());
    }
}
