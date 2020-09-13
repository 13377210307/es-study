package com.search.service;

import com.search.entity.Book;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.stream.Stream;


@Configuration
public interface BookRepository extends ElasticsearchRepository<Book,Long> {

    List<Book> findBookByName(String name);

    Page<Book> findBookByAuthorOrName(String name, Pageable pageable);

}
