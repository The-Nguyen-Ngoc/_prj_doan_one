package com.example._prj_doan.manager.controller;

import com.example._prj_doan.entity.Country;
import com.example._prj_doan.entity.Customer;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.manager.exception.CustomerNotFoundException;
import com.example._prj_doan.manager.service.CustomerService;
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
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/customers")
    public String listFirstPage(Model model) {
        return listByPage(model, 1, "firstName", "asc", null);
    }

    @GetMapping("/customers/page/{pageNum}")
    public String listByPage(Model model, @PathVariable(name = "pageNum") int pageNum,
                             @RequestParam(name = "sortField") String sortField,
                             @RequestParam(name = "sortDir") String sortDir,
                             @RequestParam(name = "keyword") String keyword) {
       Page<Customer> pageResult = customerService.listByPage(pageNum, sortField, sortDir, keyword);
       List<Customer> customers = pageResult.getContent();

        long startCount = (pageNum-1)* Constant.PRODUCTS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.PRODUCTS_IN_PAGE -1;
        long endCount = (endCountExpected > pageResult.getTotalElements()) ?
                pageResult.getTotalElements() : endCountExpected;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";


        model.addAttribute("customers", customers);
        model.addAttribute("startCount", startCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", pageResult.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", pageResult.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        return "customers/customers";
    }

    @GetMapping("/customers/{id}/enabled/{status}")
    public String updateStatus(@PathVariable(name = "id") Integer id,
                               @PathVariable(name = "status") boolean enbaled,
                               RedirectAttributes redirectAttributes) {
        customerService.updateCustomerEnabledStatus(id, enbaled);
        String status = enbaled ? "kích hoạt" : "vô hiệu hóa";
        String message = "Khách hàng " + id + " đã được " + status + " thành công";

        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/customers";
    }

    @GetMapping("/customers/edit/{id}")
    public String editCustomer(@PathVariable("id") Integer id, Model model,
                               RedirectAttributes redirectAttributes) {
        try{
            Customer customer = customerService.get(id);
            List<Country> countryList = customerService.listAllCountries();

            model.addAttribute("customer", customer);
            model.addAttribute("listCountries", countryList);
            model.addAttribute("pageTitle", "Chỉnh sửa thông tin khách hàng");

            return "customers/customer_form";
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/customers";
        }
    }

    @PostMapping("/customers/save")
    public String saveCustomer(Customer customer,Model model ,RedirectAttributes redirectAttributes) {
        customerService.save(customer);
        redirectAttributes.addFlashAttribute("message", "Cập nhật thông tin khách hàng thành công!");

        return "redirect:/customers";
    }

    @GetMapping("/customers/delete/{id}")
    public String deleteCustomer(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes){
        try {
            customerService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thông tin khách hàng thành công!");
        } catch (CustomerNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", "Có lỗi xảy ra!");

        }
        return "redirect:/customers";
    }

    @GetMapping("/customers/detail/{id}")
    public String viewCustomer(@PathVariable("id") Integer id, Model model, RedirectAttributes ra) {
        try {
            Customer customer = customerService.get(id);
            model.addAttribute("customer", customer);

            return "customers/customer_detail_modal";
        } catch (CustomerNotFoundException ex) {
            ra.addFlashAttribute("message", ex.getMessage());
            return "customers/customers";
        }
    }


}
