package com.how2java.tmall.service.impl;

import com.how2java.tmall.mapper.OrderMapper;
import com.how2java.tmall.pojo.Order;
import com.how2java.tmall.pojo.OrderExample;
import com.how2java.tmall.pojo.OrderItem;
import com.how2java.tmall.pojo.User;
import com.how2java.tmall.service.OrderItemService;
import com.how2java.tmall.service.OrderService;
import com.how2java.tmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/4/11.
 */
@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    OrderMapper orderMapper;
    @Autowired
    UserService userService;
    @Autowired
    OrderItemService orderItemService;

    public void setUser(Order o){
        User u = userService.get(o.getUid());
        o.setUser(u);
    }
    public void setUser(List<Order> os){
        for(Order o : os){
            setUser(o);
        }
    }

    @Override
    public List<Order> list() {
        OrderExample example = new OrderExample();
        example.setOrderByClause("id desc");
        List<Order> result = orderMapper.selectByExample(example);
        setUser(result);
        return result;
    }

    @Override
    public void add(Order o) {
        orderMapper.insert(o);
    }

    @Override
    public void delete(int id) {
        orderMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Order o) {
        orderMapper.updateByPrimaryKeySelective(o);
    }

    @Override
    public Order get(int id) {
        Order o = orderMapper.selectByPrimaryKey(id);
        setUser(o);
        return o;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,rollbackForClassName="Exception")
    public float add(Order o, List<OrderItem> ois) {
        float total = 0;
        add(o);

        for(OrderItem oi:ois){
            oi.setOid(o.getId());
            orderItemService.update(oi);
            total += oi.getProduct().getPromotePrice()*oi.getNumber();
        }
        return total;
    }

    @Override
    public List<Order> list(int uid, String excludedStatus) {
        OrderExample example = new OrderExample();
        example.createCriteria().andUidEqualTo(uid).andStatusNotEqualTo(excludedStatus);
        example.setOrderByClause("id desc");
        List<Order> os = orderMapper.selectByExample(example);
        return os;
    }
}
