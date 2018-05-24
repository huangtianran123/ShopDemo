package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.Product;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface OrderItemService {

    List<OrderItem> list();
    void add(OrderItem ot);
    void delete(int id);
    void update(OrderItem ot);
    OrderItem get(int id);

    void fill(Order o);
    void fill(List<Order> os);
    int getSaleCount(int pid);
    List<OrderItem> listByUser(int uid);
}
