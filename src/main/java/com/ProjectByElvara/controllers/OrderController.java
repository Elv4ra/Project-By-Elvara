package com.ProjectByElvara.controllers;

import com.ProjectByElvara.configs.PageConfig;
import com.ProjectByElvara.configs.enums.Pages;
import com.ProjectByElvara.dto.OrderDTO;
import com.ProjectByElvara.entities.additionalClasses.OrderStatus;
import com.ProjectByElvara.entities.additionalClasses.UserRole;
import com.ProjectByElvara.services.MyUserDetailsService;
import com.ProjectByElvara.services.OrderService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.sql.SQLException;
import java.util.Objects;

@Controller
public class OrderController {
    private final OrderService orderService;
    private final MyUserDetailsService myUserDetailsService;

    public OrderController(OrderService orderService, UserDetailsService myUserDetailsService) {
        this.orderService = orderService;
        this.myUserDetailsService = (MyUserDetailsService) myUserDetailsService;
    }

    @GetMapping(value = "/cart")
    public ModelAndView openCartPage() {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.CART));
        modelAndView.addObject("newOrder", new OrderDTO());
        return modelAndView;
    }

    @PostMapping(value = "/addOrder")
    public RedirectView addNewOrder(@ModelAttribute(name = "newOrder") OrderDTO newOrder,
                                    RedirectAttributes redirectAttributes) throws SQLException {
        Integer ordersUserId = this.orderService.addNewOrder(newOrder, this.myUserDetailsService.getSessionUserId());
        redirectAttributes.addFlashAttribute("message", "Ваше замолвення було успішно створено");
        return new RedirectView("/orders?userId=" + ordersUserId);
    }

    @GetMapping(value = "/orders")
    public ModelAndView openOrdersPage(@RequestParam(required = false, name = "userId") String userId,
                                       @RequestParam(required = false, name = "status") OrderStatus status) throws SQLException {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.ORDERS));
        modelAndView.addObject("status", status);
        modelAndView.addObject("orders", this.orderService.getAllOrders(status,
                userId == null ? null : (this.myUserDetailsService.hasAccess(UserRole.ROLE_ADMIN) ? Integer.parseInt(userId) : this.myUserDetailsService.getSessionUserId())));
        return modelAndView;
    }

    @GetMapping(value = "/order")
    public ModelAndView openOrderPage(@RequestParam(name = "id") String id) throws SQLException {
        ModelAndView modelAndView = new ModelAndView(PageConfig.getInstance().getProperty(Pages.ORDER));
        OrderDTO order = this.orderService.getOrderById(Integer.parseInt(id));
        if (!this.myUserDetailsService.hasAccess(UserRole.ROLE_ADMIN)
                && !Objects.equals(order.getUser().getId(), this.myUserDetailsService.getSessionUserId())) {
            return new ModelAndView(
                    new RedirectView("/orders?userId=" + this.myUserDetailsService.getSessionUserId())
            );
        }
        modelAndView.addObject("order", order);
        return modelAndView;
    }

    @PostMapping(value = "/updateOrderStatus")
    public RedirectView updateOrderStatus(@RequestParam(name = "orderId") String orderId,
                                          @RequestParam(name = "status") OrderStatus status,
                                          RedirectAttributes redirectAttributes) throws SQLException {
        this.orderService.updateOrderStatus(Integer.parseInt(orderId), status);
        redirectAttributes.addFlashAttribute("message", "Статус було успішно змінено");
        return new RedirectView("/order?id=" + orderId);
    }
}
