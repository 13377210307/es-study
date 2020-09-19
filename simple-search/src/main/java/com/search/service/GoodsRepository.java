package com.search.service;

import com.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

/**
 * 自定义查询类，继承ElasticsearchRepository
 * 参数一：索引类
 * 参数二：索引id类型
 */
@Service
public interface GoodsRepository extends ElasticsearchRepository<Goods,String> {
}
