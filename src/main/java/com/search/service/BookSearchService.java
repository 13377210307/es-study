package com.search.service;

import com.search.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookSearchService {

    // 创建文档
    void saveBook(Book book);

    // 查询全部(terms查询或match_all查询）
    List<Book> queryAll();

    // term查询
    List<Book> termQuery(String name);

    // match查询
    List<Book> matchQuery(String param);

    // 通过id查询
    Book findById(Long id);

    // 布尔查询
    Page<Book> findByAuthorOrName(String name);
}
