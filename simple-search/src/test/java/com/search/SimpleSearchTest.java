package com.search;

import com.search.entity.Goods;
import com.search.service.GoodsRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SimpleSearchTest {

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private GoodsRepository goodsRepository;

    /**
     * 创建索引：指定需要创建索引的类
     *
     * 1：创建索引
     * 2：创建映射
     */
    @Test
    public void createIndex() {
        //new Goods("1","OPPO手机", 2300.56,"手机","OPPO",
        //                "//img10.360buyimg.com/n1/s450x450_jfs/t1/116953/38/11230/162521/5ef9a041Ea0b31b85/c97e3c267b1718bd.jpg");
        this.elasticsearchRestTemplate.createIndex(Goods.class);
        this.elasticsearchRestTemplate.putMapping(Goods.class);
    }

    /**
     * 判断索引是否存在，不存在会直接进行创建
     */
    @Test
    public void IndexIsExist() {
        boolean result = this.elasticsearchRestTemplate.indexExists(Goods.class);
        System.out.println(result);
    }


    /**
     * 删除索引
     *
     * 1：根据类名删除索引
     */
    @Test
    public void deleteIndex() {
        this.elasticsearchRestTemplate.deleteIndex(Goods.class);
    }


    /**
     * 新增数据
     */
    @Test
    public void createDocument() {
        Goods goods = new Goods("1", "OPPO手机", 2300.56, "手机", "OPPO",
                "//img10.360buyimg.com/n1/s450x450_jfs/t1/116953/38/11230/162521/5ef9a041Ea0b31b85/c97e3c267b1718bd.jpg");

        this.goodsRepository.save(goods);
    }

    /**
     * 批量新增数据
     */
    @Test
    public void saveDocuments() {
        List<Goods> goods = new ArrayList<>();
        Goods good1 = new Goods("2", "华为荣耀手机", 2600.56, "手机", "华为",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good1);

        Goods good2 = new Goods("3", "Redmi", 1600.56, "手机", "红米",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good2);

        Goods good3 = new Goods("4", "Apple手机", 5600.56, "手机", "Apple",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        goods.add(good3);

        this.goodsRepository.saveAll(goods);
    }

    /**
     * 修改某个数据
     */
    @Test
    public void updateDocument() {
        Goods good1 = new Goods("1", "OPPO手机", 2300.56, "手机", "OPPO",
                "//img11.360buyimg.com/n1/s450x450_jfs/t1/104812/35/17303/242507/5e848fb1E2c1d6eff/d2db1412dcce1d96.jpg");
        this.goodsRepository.save(good1);
    }

    /**
     * 查询某个数据
     */
    @Test
    public void queryOne() {
        Optional<Goods> goods = this.goodsRepository.findById("1");
        System.out.println(goods);
    }

    /**
     * 查询全部数据
     */
    @Test
    public void queryAll() {
        Iterable<Goods> goods = this.goodsRepository.findAll();
        goods.forEach(System.out:: println);
    }


}
