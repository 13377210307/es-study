package com.search.service;

import com.search.entity.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GoodsRepository extends ElasticsearchRepository<Goods,String> {

    // 根据名称以及品牌精确查询
    List<Goods> getAllByNameAndBrand(String name,String barnd);
}
