package com.example._prj_doan.manager.controller;

import com.example._prj_doan.entity.Order;
import com.example._prj_doan.entity.Setting;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.manager.exception.OrderNotFoundException;
import com.example._prj_doan.manager.service.OrderService;
import com.example._prj_doan.manager.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private SettingService settingService;

    @GetMapping("/orders")
    public String listFirstPage() {
        return  "redirect:/orders/page/1?sortField=country&sortDir=asc&keyword="; }

    @GetMapping("/orders/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @RequestParam(name = "sortField") String sortField,
                             @RequestParam(name = "sortDir") String sortDir,
                             @RequestParam(name = "keyword") String keyword,
                             HttpServletRequest request) {
        Page<Order> pageResult = orderService.listByPage(pageNum, sortField, sortDir, keyword);
        loadCurrencySetting(request);
        long startCount = (pageNum-1)* Constant.PRODUCTS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.PRODUCTS_IN_PAGE -1;
        long endCount = (endCountExpected > pageResult.getTotalElements()) ?
                pageResult.getTotalElements() : endCountExpected;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("startCount", startCount);
        model.addAttribute("orders", pageResult.getContent());
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", pageResult.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageResult.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "orders/orders";
    }

    private void loadCurrencySetting(HttpServletRequest request) {
        List<Setting> settings = settingService.getCurrencySettings();

        for(Setting setting : settings) {
            request.setAttribute(setting.getKey(), setting.getValue());
        }
    }

    @GetMapping("/orders/detail/{id}")
    public String viewOrderDetails(@PathVariable("id")Integer id, Model model,
                                   RedirectAttributes attributes, HttpServletRequest request) {
        Order order = null;
        try {
            order = orderService.get(id);
            loadCurrencySetting(request);
            model.addAttribute("order", order);
            return "orders/order_details_modal";
        } catch (OrderNotFoundException e) {
           attributes.addFlashAttribute("message", e.getMessage());
           return "redirect:/orders";
        }

    }

    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            orderService.delete(id);
        attributes.addFlashAttribute("message", "Xóa Đơn Đặt Hàng Thành Công!");
        } catch (OrderNotFoundException e) {
            attributes.addFlashAttribute("message", e.getMessage());

        }
        return "redirect:/orders/page/1?sortField=country&sortDir=asc&keyword=";
    }
}

