package com.example._prj_doan.manager.controller;

import com.example._prj_doan.entity.Country;
import com.example._prj_doan.entity.ShippingRate;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.manager.exception.ShippingRateAlreadyException;
import com.example._prj_doan.manager.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class ShippingRateController {
    @Autowired
    private ShippingRateService shippingRateService;

    @GetMapping("/shipping_rates")
    public String listAll() {
        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc&keyword=";
    }

    @GetMapping("/shipping_rates/page/{pageNum}")
    public String listByPage(@PathVariable(name ="pageNum") int pageNum, Model model,
                             @RequestParam("sortField") String sortField,
                             @RequestParam("sortDir") String sortDir,
                             @RequestParam("keyword") String keyword){
        Page<ShippingRate> shippingRates = shippingRateService.listByPage(pageNum, sortField, sortDir, keyword);

        long startCount = (pageNum-1)* Constant.USERS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.USERS_IN_PAGE -1;
        long endCount = (endCountExpected > shippingRates.getTotalElements()) ?
                shippingRates.getTotalElements() : endCountExpected;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("startCount", startCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", shippingRates.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("shippingRates", shippingRates.getContent());
        model.addAttribute("totalItems", shippingRates.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("titlePage", "Quản Lý Vận Chuyển");

        return "shipping_rates/shipping_rates";
    }

    @GetMapping("/shipping_rates/new")
    public String newRate(Model model){
        List<Country> countryList = shippingRateService.listAllCountries();
        model.addAttribute("rate", new ShippingRate());
        model.addAttribute("countryList", countryList);
        model.addAttribute("pageTitle", "Thêm Mới Vận Đơn");
        return "shipping_rates/shipping_rate_form";
    }

    @PostMapping("/shipping_rates/save")
    public String save(ShippingRate shippingRate, RedirectAttributes redirectAttributes){

        try {
            shippingRateService.save(shippingRate);
            redirectAttributes.addFlashAttribute("message", "Thêm Vận Đơn Mới Thành Công");
        } catch (ShippingRateAlreadyException e) {
           redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc&keyword=";
    }

    @GetMapping("/shipping_rates/cod/{id}/enabled/{supported}")
    public String updateCODSupport(@PathVariable(name = "id") Integer id,
                                   @PathVariable(name = "supported") Boolean supported,
                                   Model model, RedirectAttributes redirectAttributes){
        try {
            shippingRateService.updateCODSupport(id, supported);
            redirectAttributes.addFlashAttribute("message", "Cập Nhật Thành Công");
        } catch (ShippingRateAlreadyException e) {
           redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc&keyword=";
    }

    @GetMapping("/shipping_rates/delete/{id}")
    public String deleteRate(@PathVariable(name = "id") Integer id,
                             Model model, RedirectAttributes redirectAttributes){
        try {
            shippingRateService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Xóa Thành Công");
        } catch (ShippingRateAlreadyException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc&keyword=";
    }

    @GetMapping("/shipping_rates/edit/{id}")
    public String editRate(@PathVariable(name = "id") Integer id, Model model,
                           RedirectAttributes redirectAttributes){
        try {
            ShippingRate rate = shippingRateService.get(id);
            List<Country> countryList = shippingRateService.listAllCountries();

            model.addAttribute("countryList", countryList);
            model.addAttribute("rate", rate);
            model.addAttribute("pageTitle", "Chỉnh Sửa Vận Đơn");

            return "shipping_rates/shipping_rate_form";
        } catch (ShippingRateAlreadyException e) {
           redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/shipping_rates/page/1?sortField=country&sortDir=asc&keyword=";
    }

}
