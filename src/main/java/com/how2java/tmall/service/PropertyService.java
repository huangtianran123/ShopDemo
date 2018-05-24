package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Property;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface PropertyService {

    List list(int cid);
    void add(Property p);
    void delete(int id);
    void update(Property p);
    Property get(int id);

}
