package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Review;

import java.util.List;

/**
 * Created by Administrator on 2018/4/23.
 */
public interface ReviewService {

    List<Review> list(int pid);

    void add(Review review);

    void delete(int id);

    void update(Review review);

    Review get(int id);

    int getCount(int pid);
}
