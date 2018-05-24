package com.how2java.tmall.service;

import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderItem;

import java.util.List;

/**
 * Created by Administrator on 2018/4/9.
 */
public interface OrderService {

    String waitPay = "waitPay";
    String waitDelivery = "waitDelivery";
    String waitConfirm = "waitConfirm";
    String waitReview = "waitReview";
    String finish = "finish";
    String delete = "delete";

    List<Order> list();
    void add(Order o);
    void delete(int id);
    void update(Order o);
    Order get(int id);
    float add(Order o, List<OrderItem> ois);
    List<Order> list(int uid, String excludedStatus);
}
