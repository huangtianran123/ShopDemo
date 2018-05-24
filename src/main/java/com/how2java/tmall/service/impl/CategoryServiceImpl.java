package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.CategoryMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.CategoryExample;
import com.how2java.tmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Category> list() {
        CategoryExample example = new CategoryExample();
        example.setOrderByClause("id desc");
        return categoryMapper.selectByExample(example);
    }

    @Override
    public void add(Category c) {
        categoryMapper.insert(c);
    }

    @Override
    public void delete(int id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Category c) {
        categoryMapper.updateByPrimaryKeySelective(c);
    }

    @Override
    public Category get(int id) {
        Category c = categoryMapper.selectByPrimaryKey(id);
        return c;
    }
}
