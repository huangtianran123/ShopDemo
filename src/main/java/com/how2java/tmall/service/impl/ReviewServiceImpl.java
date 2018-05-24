package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.ReviewMapper;
import com.how2java.tmall.pojo.Category;
import com.how2java.tmall.pojo.Review;
import com.how2java.tmall.pojo.ReviewExample;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.ProductService;
import com.how2java.tmall.service.ReviewService;
import com.how2java.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    @Autowired
    ReviewMapper reviewMapper;
    @Autowired
    UserService userService;

    public void setUser(Review review){
        User user = userService.get(review.getUid());
        review.setUser(user);
    }
    public void setUser(List<Review> rs){
        for(Review r : rs){
            setUser(r);
        }
    }

    @Override
    public List<Review> list(int pid) {
        ReviewExample example = new ReviewExample();
        example.createCriteria().andPidEqualTo(pid);
        List<Review> result = reviewMapper.selectByExample(example);
        setUser(result);
        return result;
    }

    @Override
    public void add(Review review) {
        reviewMapper.insert(review);
    }

    @Override
    public void delete(int id) {
        reviewMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Review review) {
        reviewMapper.updateByPrimaryKeySelective(review);
    }

    @Override
    public Review get(int id) {
        return reviewMapper.selectByPrimaryKey(id);
    }

    @Override
    public int getCount(int pid) {
        return list(pid).size();
    }
}
