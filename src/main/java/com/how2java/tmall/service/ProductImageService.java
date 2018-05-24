package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductImage;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface ProductImageService {

    String type_single = "type_single";
    String type_detail = "type_detail";

    void add(ProductImage pi);
    void delete(int id);
    void update(ProductImage pi);
    ProductImage get(int id);
    List<ProductImage> list(int pid, String type);

}
