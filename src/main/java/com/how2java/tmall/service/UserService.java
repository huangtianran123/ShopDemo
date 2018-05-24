package com.how2java.tmall.service;

import com.how2java.tmall.pojo.User;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface UserService {

    List<User> list();
    void add(User c);
    void delete(int id);
    void update(User c);
    User get(int id);

    boolean isExist(String name);
    User get(String name, String password);
}
