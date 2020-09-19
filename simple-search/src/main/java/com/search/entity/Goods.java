package com.search.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;


/**
 * 使用@Document表示es索引库
 * indexName：指索引名称
 * type：在7.x之后,es没有type，所以使用固定值_doc
 * @Id表示为es主键
 * @Field表示该字段为es字段   analyzer：使用分词工具   type表示字段类型
 * index = false：表示当前字段不创建索引
 */
@Data
@Document(indexName = "goods",type = "_doc")
public class Goods implements Serializable {

    @Id
    private String id;

    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String name;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Keyword,index = false)
    private String image;

    public Goods() {
    }

    public Goods(String id, String name, Double price, String category, String brand, String image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
        this.brand = brand;
        this.image = image;
    }
}
