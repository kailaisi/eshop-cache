package com.kailaisi.eshopcache.model;

import java.util.List;

/**
 * 商品信息
 */
public class ProductInfo {

    private Long id;
    private String name;
    private Double price;
    /**
     * id : 1000
     * price : 5999
     * pictureList : ["a.png","b.png","c.png"]
     * color : ["红色","白色","绿色 "]
     */
    private List<String> pictureList;
    private List<String> color;

    public ProductInfo(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(List<String> pictureList) {
        this.pictureList = pictureList;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }
}
