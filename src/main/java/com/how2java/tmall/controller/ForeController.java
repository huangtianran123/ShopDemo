package com.how2java.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.how2java.tmall.comparator.*;
import com.how2java.tmall.pojo.*;
import com.how2java.tmall.service.*;
import org.apache.commons.lang.math.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/4/21.
 */
@Controller
@RequestMapping("")
public class ForeController {

    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    ReviewService reviewService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;

    @RequestMapping("forehome")
    public String home(Model model){
        List<Category> cs = categoryService.list();
        productService.fill(cs);
        productService.fillByRow(cs);
        model.addAttribute("cs",cs);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model, User user){
        String name = user.getName();
        name = HtmlUtils.htmlEscape(name);
        user.setName(name);
        boolean exist = userService.isExist(name);
        if(exist){
            String m = "用户名子已经被注册过了不能用哦嘿嘿嘿";
            model.addAttribute("msg",m);
            model.addAttribute("user",null);
            return "fore/register";
        }
         userService.add(user);
        return "redirect:registerSuccessPage";
    }

    @RequestMapping("forelogin")
    public String login(String name, String password, HttpSession session, Model model){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name,password);

        if(user==null){
            model.addAttribute("msg","账号密码错误");
            return "fore/login";
        }
        session.setAttribute("user",user);
        return "redirect:forehome";
    }

    @RequestMapping("forelogout")
    public String loggout(HttpSession session){
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product(int pid, Model model){
        Product p = productService.get(pid);

        List<ProductImage> productSingleImages = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> productDetailImages = productImageService.list(pid, ProductImageService.type_detail);
        p.setProductSingleImages(productSingleImages);
        p.setProductDetailImages(productDetailImages);

        List<Review> reviews = reviewService.list(pid);
        List<PropertyValue> pvs = propertyValueService.list(pid);
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("p",p);
        model.addAttribute("pvs",pvs);
        model.addAttribute("reviews",reviews);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin(HttpSession session){
        User user = (User) session.getAttribute("user");
        if(user == null)
            return "fail";
        return "success";
    }

    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(String name, String password, HttpSession session){
        name = HtmlUtils.htmlEscape(name);
        User user = userService.get(name, password);
        if(user==null){
            return "fail";
        }
        session.setAttribute("user",user);
        return "success";
    }

    @RequestMapping("forecategory")
    public String category(int cid, String sort, Model model){
        Category c = categoryService.get(cid);
        productService.fill(c);
        productService.setSaleAndReviewNumber(productService.list(cid));
        if(sort!=null){
            switch (sort){
                case "review":
                    Collections.sort(c.getProducts(), new ProductReviewComparator());
                    break;
                case "date":
                    Collections.sort(c.getProducts(), new ProductDateComparator());
                    break;
                case "price":
                    Collections.sort(c.getProducts(), new ProductPriceComparator());
                    break;
                case "saleCount":
                    Collections.sort(c.getProducts(), new ProductSaleCountComparator());
                    break;
                case "all":
                    Collections.sort(c.getProducts(), new ProductAllComparator());
                    break;
            }
        }
        model.addAttribute("c",c);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search(String keyword, Model model){
        PageHelper.offsetPage(0,20);
        List<Product> ps = productService.search(keyword);
        productService.setSaleAndReviewNumber(ps);
        model.addAttribute("ps",ps);
        return "fore/searchResult";
    }

    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session){
        Product p = productService.get(pid);
        int oiid = 0;
        boolean found = false;
        User user = (User)session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for(OrderItem oi : ois){
            if(oi.getProduct().getId().intValue() == p.getId().intValue()){
                oi.setNumber(oi.getNumber()+num);
                found = true;
                oiid = oi.getId();
                orderItemService.update(oi);
                break;
            }
        }
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setNumber(num);
            oi.setPid(pid);
            oi.setUid(user.getId());
            orderItemService.add(oi);
            oiid = oi.getId();
        }
        return "redirect:forebuy?oiid=" + oiid;
    }

    @RequestMapping("forebuy")
    public String buy(Model model, String[] oiid, HttpSession session){
        List<OrderItem> ois = new ArrayList<>();
        float total = 0;
        for(String strid : oiid){
            int id = Integer.parseInt(strid);
            OrderItem oi = orderItemService.get(id);
            total += oi.getProduct().getPromotePrice() * oi.getNumber();
            ois.add(oi);
        }
        session.setAttribute("ois",ois);
        model.addAttribute("total",total);
        return "fore/buy";
    }

    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, HttpSession session){
        Product p = productService.get(pid);
        User user = (User)session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        boolean found = false;
        for(OrderItem oi : ois){
            if(oi.getProduct().getId().intValue() == p.getId().intValue()){
                oi.setNumber(oi.getNumber() + num);
                found = true;
                orderItemService.update(oi);
                break;
            }
        }
        if(!found){
            OrderItem oi = new OrderItem();
            oi.setNumber(num);
            oi.setPid(pid);
            oi.setUid(user.getId());
            orderItemService.add(oi);
        }
        return "success";
    }

    @RequestMapping("forecart")
    public String cart(Model model, HttpSession session){
        User user = (User) session.getAttribute("user");
        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        model.addAttribute("ois",ois);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem(HttpSession session, int pid, int number) {
        User user =(User)  session.getAttribute("user");
        if(null==user)
            return "fail";

        List<OrderItem> ois = orderItemService.listByUser(user.getId());
        for (OrderItem oi : ois) {
            if(oi.getProduct().getId().intValue()==pid){
                oi.setNumber(number);
                orderItemService.update(oi);
                break;
            }

        }
        return "success";
    }

    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem(HttpSession session, int oiid){
        User user = (User) session.getAttribute("user");
        if(user==null)
            return "fail";
        orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder(HttpSession session, Order order){
        List<OrderItem> ois = (List<OrderItem>) session.getAttribute("ois");
        User user = (User)session.getAttribute("user");
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())+ RandomUtils.nextInt(10000);
        order.setStatus(OrderService.waitPay);
        order.setDeliveryDate(new Date());
        order.setOrderCode(orderCode);
        order.setUid(user.getId());

        float total = orderService.add(order, ois);
        return "redirect:forealipay?oid="+order.getId() +"&total="+total;
    }

    @RequestMapping("forepayed")
    public String payed(Model model, int oid, float total){
        Order order = orderService.get(oid);
        order.setPayDate(new Date());
        order.setStatus(OrderService.waitDelivery);
        orderService.update(order);
        model.addAttribute("order",order);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought(HttpSession session, Model model){
        User user = (User) session.getAttribute("user");
        List<Order> os = orderService.list(user.getId(), OrderService.delete);
        orderItemService.fill(os);
        model.addAttribute("os",os);
        return "fore/bought";
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay(Model model, int oid){
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        model.addAttribute("o", o);
        return "fore/confirmPay";
    }

    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed(Model model, int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.waitReview);
        o.setConfirmDate(new Date());
        orderService.update(o);
        return "fore/orderConfirmed";
    }

    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder(Model model, int oid){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.delete);
        orderService.update(o);
        return "success";
    }

    @RequestMapping("forereview")
    public String review(Model model, int oid){
        Order o = orderService.get(oid);
        orderItemService.fill(o);
        Product p = o.getOrderItems().get(0).getProduct();
        List<Review> reviews = reviewService.list(p.getId());
        productService.setSaleAndReviewNumber(p);
        model.addAttribute("o",o);
        model.addAttribute("p",p);
        model.addAttribute("reviews",reviews);
        return "fore/review";
    }

    @RequestMapping("foredoreview")
    public String doreview(Model model, int oid, int pid, HttpSession session, String content){
        Order o = orderService.get(oid);
        o.setStatus(OrderService.finish);
        orderService.update(o);
        Product p = productService.get(pid);
        content = HtmlUtils.htmlEscape(content);
        User user = (User) session.getAttribute("user");
        Review review = new Review();
        review.setPid(pid);
        review.setCreateDate(new Date());
        review.setContent(content);
        review.setUid(user.getId());
        reviewService.add(review);
        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
}
