package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Category;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface CategoryService {

    List<Category> list();
    void add(Category c);
    void delete(int id);
    void update(Category c);
    Category get(int id);

}
