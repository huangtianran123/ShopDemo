package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import sun.awt.geom.AreaOp;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface ProductService {

    List<Product> list(int cid);
    void add(Product p);
    void delete(int id);
    void update(Product p);
    Product get(int id);
    void setFirstProductImage(Product p);

    void fill(Category c);
    void fill(List<Category> cs);
    void fillByRow(List<Category> cs);

    void setSaleAndReviewNumber(Product p);
    void setSaleAndReviewNumber(List<Product> ps);
    List<Product> search(String keyword);
}
