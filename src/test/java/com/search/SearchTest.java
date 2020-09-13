package com.search;

import com.search.entity.Book;
import com.search.service.BookRepository;
import com.search.service.BookSearchService;
import org.elasticsearch.action.index.IndexRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SearchTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookSearchService bookSearchService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    // 查看索引是否存在
    @Test
    public void queryIndex() {
        boolean result = this.elasticsearchRestTemplate.indexExists("book");
        System.out.println(result);
    }

    // 创建索引
    @Test
    public void createIndex() {
        String indexName = "test";
        boolean result = this.elasticsearchRestTemplate.createIndex(indexName);
        System.out.println(result);
    }

    /**
     * 删除索引
     */
    @Test
    public void deleteIndex() {
        boolean result = this.elasticsearchRestTemplate.deleteIndex("test");
        System.out.println(result);
    }

    /**
     * 查询全部
     */
    @Test
    public void finaAll() {
        Iterable<Book> books = this.bookRepository.findAll();
        books.forEach(book -> {
            System.out.println(book.getName());
        });
    }

    /**
     * term查询
     */
    @Test
    public void termQuery() {
        List<Book> books = this.bookRepository.findBookByName("测试");
        books.forEach(book -> {
            System.out.println(book.getName());
        });
    }

    /**
     * 新增文档
     */
    @Test
    public void saveDoc() {
       Book book = new Book();
       book.setId(7L);
       book.setAuthor("测试作者");
       book.setName("测试书籍");
       book.setPrice(49.52);
       book.setDescr("从方法名称派生查询并不总是足够的，并且/或者可能导致方法名称不可读。在这种情况下，可以使用@Query注释（请参阅使用@Query注释）。");
       this.bookSearchService.saveBook(book);
    }

    /**
     * 根据id查询
     */
    @Test
    public void getById() {
        Long id = 1L;
        Book book = this.bookSearchService.findById(id);
        System.out.println(book.getName());
    }


    /**
     * 布尔查询
     */
    @Test
    public void BoolQuery() {
        String name = "测试作者";
        Page<Book> books = this.bookSearchService.findByAuthorOrName(name);
        if (!CollectionUtils.isEmpty(books.getContent())) {
            books.forEach(book -> {
                System.out.println(book.getName());
            });
        }
    }
}
