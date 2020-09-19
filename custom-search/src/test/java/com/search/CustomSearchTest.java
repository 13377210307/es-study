package com.search;

import com.search.entity.Goods;
import com.search.service.GoodsRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomSearchTest {

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 根据名称与品牌精确查询
     */
    @Test
    public void queryGoodsByNameAndBrand() {
        String name = "OPPO手机";
        String brand = "华为";

        List<Goods> goods = this.goodsRepository.getAllByNameAndBrand(name, brand);

        if (!CollectionUtils.isEmpty(goods)) {
            goods.forEach(good -> {
                System.out.println(good.getName());
            });
        }
    }

    /**
     * 高级查询
     */
    @Test
    public void queryGoodsByNameOrBrand() {
        String name = "OPPO手机";
        String brand = "华为";

        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        boolQueryBuilder.should(QueryBuilders.matchQuery("name",name)).should(QueryBuilders.matchQuery("brand",brand));

        queryBuilder.withQuery(boolQueryBuilder);

        List<Goods> goods = this.goodsRepository.search(queryBuilder.build()).getContent();

        if (!CollectionUtils.isEmpty(goods)) {
            goods.forEach(good -> {
                System.out.println(good.getName());
            });
        }
    }
}
