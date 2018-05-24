package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ProductMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Product;
import com.how2java.tmall.pojo.ProductExample;
import com.how2java.tmall.pojo.ProductImage;
import com.how2java.tmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/4/10.
 */
@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    ProductMapper productMapper;
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewServicel;

    public void setCategory(Product p){
        Category c = categoryService.get(p.getCid());
        p.setCategory(c);
    }

    public void setCategory(List<Product> ps){
        for(Product p : ps){
            setCategory(p);
        }
    }

    @Override
    public List<Product> list(int cid) {
        ProductExample example = new ProductExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        List<Product> result = productMapper.selectByExample(example);
        setCategory(result);
        setFirstProductImage(result);
        return result;
    }

    @Override
    public void add(Product p) {
        productMapper.insert(p);
    }

    @Override
    public void delete(int id) {
        productMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Product p) {
        productMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public Product get(int id) {
        Product p = productMapper.selectByPrimaryKey(id);
        setCategory(p);
        setFirstProductImage(p);
        return p;
    }

    @Override
    public void setFirstProductImage(Product p) {
        List<ProductImage> pis = productImageService.list(p.getId(), ProductImageService.type_single);
        if(!pis.isEmpty()) {
            ProductImage pi = pis.get(0);
            p.setFirstProductImage(pi);
        }
    }

    public void setFirstProductImage(List<Product> ps){
        for(Product p : ps){
            setFirstProductImage(p);
        }
    }

    @Override
    public void fill(Category c) {
        List<Product> ps = list(c.getId());
        c.setProducts(ps);
    }

    @Override
    public void fill(List<Category> cs) {
        for(Category c : cs){
            fill(c);
        }
    }

    @Override
    public void fillByRow(List<Category> cs) {
        int productNumberEachRow = 8;
        for(Category c : cs){
            List<Product> ps = c.getProducts();
            List<List<Product>> productsByRow = new ArrayList<>();
            for(int i = 0;i < ps.size(); i += productNumberEachRow){
                int size = i + productNumberEachRow;
                size = size>ps.size()?ps.size():size;
                List<Product> productsOfEachRow = ps.subList(i, size);
                productsByRow.add(productsOfEachRow);
            }
            c.setProductsByRow(productsByRow);
        }
    }

    @Override
    public void setSaleAndReviewNumber(Product p) {
        int saleCount = orderItemService.getSaleCount(p.getId());
        int reviewCount = reviewServicel.getCount(p.getId());
        p.setSaleCount(saleCount);
        p.setReviewCount(reviewCount);
    }

    @Override
    public void setSaleAndReviewNumber(List<Product> ps) {
        for(Product p : ps){
            setSaleAndReviewNumber(p);
        }
    }

    @Override
    public List<Product> search(String keyword) {
        ProductExample example = new ProductExample();
        example.createCriteria().andNameLike("%" + keyword + "%");
        List<Product> ps = productMapper.selectByExample(example);
        setFirstProductImage(ps);
        setCategory(ps);
        return ps;
    }
}
