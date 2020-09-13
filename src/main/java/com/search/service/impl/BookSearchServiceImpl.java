package com.search.service.impl;

import com.search.entity.Book;
import com.search.service.BookRepository;
import com.search.service.BookSearchService;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookSearchServiceImpl implements BookSearchService {

    private Pageable pageable = PageRequest.of(0,10);

    @Autowired
    private BookRepository bookRepository;

    /**
     * 新增文档
     */
    @Override
    public void saveBook(Book book) {
        this.bookRepository.save(book);
    }

    /**
     * 查询全部(terms查询）
     */
    @Override
    public List<Book> queryAll() {
        return null;
    }


    /**
     * term查询 term 查询是代表完全匹配，搜索之前不会对你搜索的关键字进行分词，直接拿 关键字 去文档分词库中匹配内容
     */
    @Override
    public List<Book> termQuery(String name) {
        return this.bookRepository.findBookByName(name);
    }

    /**
     * match查询 如果查询的内容是一个可以被分词的内容（text）,match 查询会将你指定的内容根据一定的方式进行分词，去分词库中匹配指定的内容
     */
    @Override
    public List<Book> matchQuery(String param) {
        return null;
    }

    /**
     * 通过id查询
     * @return
     */
    @Override
    public Book findById(Long id) {
        Optional<Book> book = this.bookRepository.findById(id);
        return book.get();
    }

    /**
     * 布尔查询
     */
    @Override
    public Page<Book> findByAuthorOrName(String name) {
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(QueryBuilders.matchQuery("name",name)).should(QueryBuilders.matchQuery("author",name));

        queryBuilder.withQuery(boolQueryBuilder);
        Page<Book> search = this.bookRepository.search(queryBuilder.build());
        return search;
    }
}
