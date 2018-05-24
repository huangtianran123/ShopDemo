package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.PropertyValue;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface PropertyValueService {

    void init(Product p);
    List<PropertyValue> list(int pid);
    void update(PropertyValue pv);
    PropertyValue get(int ptid, int pid);

}
