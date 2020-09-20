package com.search;

import com.search.entity.Goods;
import com.search.service.GoodsRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CustomSearchTest {

    @Autowired
    private GoodsRepository goodsRepository;


    /**
     * 新增数据
     */
    @Test
    public void saveMore() {
        List<Goods> goods = new ArrayList<>();
        Goods good1 = new Goods("2", "华为荣耀手机", 2600.56, "手机", "华为",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good1);

        Goods good3 = new Goods("4", "Apple手机", 5600.56, "手机", "Apple",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good3);

        Goods goods1 = new Goods("5","VIVO手机",2000.47,"手机","VIVO",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(goods1);

        Goods goods2 = new Goods("6","华为p30",4000.47,"手机","华为",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(goods2);

        Goods goods3 = new Goods("7","小米10",3999.47,"手机","小米",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(goods3);

        Goods goods4 = new Goods("3","Redmi",1600.56,"手机","小米",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(goods4);

        Goods good7 = new Goods("1", "OPPO手机", 2300.56, "手机", "OPPO",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good7);

        this.goodsRepository.saveAll(goods);

        //Iterable<Goods> goods = this.goodsRepository.findAll();
        //goods.forEach(System.out::println);
    }



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


    /**
     * java.lang.ClassCastException:
     * class org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms cannot be cast to class org.elasticsearch.search.aggregations.bucket.terms.StringTerms
     * (org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms and org.elasticsearch.search.aggregations.bucket.terms.StringTerms are in unnamed module of loader 'app')
     * 在之前的版本中是可以的, 但在 7 版本以上就不好使了.
     * 需要将 StringTerms 类型改为 Terms 类型.
     */
    /**
     * 聚合查询：根据品牌分组
     */
    @Test
    public void bucketQuery() {

        // 初始化自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 添加聚合：使用AggregationBuilders构造聚合，terms中的name表示聚合名称，field表示需要聚合的字段
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand"));

        // 添加聚合过滤，不包含任何字段，参数一表示聚合包含的字段，参数2表示排除的字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{},null));

        // 执行聚合查询
        AggregatedPage<Goods> googPage = (AggregatedPage<Goods>)this.goodsRepository.search(queryBuilder.build());

        // 解析聚合结果集，根据聚合的类型以及字段类型进行强转
        Terms brandAgg = (Terms)googPage.getAggregation("brandAgg");

        // 获取桶集合
        List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();

        // 遍历
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());
        });

    }

    /**
     * 聚合查询：根据品牌分组且算出全部均价
     */
    @Test
    public void bucketSubQuery() {

        // 初始化自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();

        // 添加聚合：使用AggregationBuilders构造聚合，terms中的name表示聚合名称，field表示需要聚合的字段
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand")
                .subAggregation(AggregationBuilders.avg("price_avg").field("price")));

        // 添加聚合过滤，不包含任何字段，参数一表示聚合包含的字段，参数2表示排除的字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));

        // 执行聚合查询
        AggregatedPage<Goods> googPage = (AggregatedPage<Goods>) this.goodsRepository.search(queryBuilder.build());

        // 解析聚合结果集，根据聚合的类型以及字段类型进行强转
        Terms brandAgg = (Terms) googPage.getAggregation("brandAgg");

        // 获取桶集合
        List<? extends Terms.Bucket> buckets = brandAgg.getBuckets();

        // 遍历
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());

            // 获取子聚合的map集合：key：聚合名称，value：子聚合对象
            Map<String, Aggregation> stringAggregationMap = bucket.getAggregations().asMap();
            Avg price = (Avg)stringAggregationMap.get("price_avg");
            System.out.println(price.getValue());
        });
    }



}
