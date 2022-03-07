package com.example._prj_doan.manager.controller;


import com.example._prj_doan.anotation.PagingAndSortingHelper;
import com.example._prj_doan.anotation.PagingAndSortingParam;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.entity.Role;
import com.example._prj_doan.entity.User;
import com.example._prj_doan.manager.exception.UserNotFoundExeption;
import com.example._prj_doan.manager.service.UserService;
import com.example._prj_doan.manager.utils.FileUploadUtil;
import com.example._prj_doan.manager.utils.UserCsvExporter;
import com.example._prj_doan.manager.utils.UserPdfExporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author TheNN
 * @since 2/12/2021
 */

@Controller

public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    public String listAll() {
        return "redirect:/users/page/1?sortField=firstName&sortDir=asc&keyword=";

    }

    @GetMapping("/users/page/{pageNum}")
    public String listByPage(@PagingAndSortingParam PagingAndSortingHelper helper,
                             @PathVariable(name ="pageNum") int pageNum, Model model,
                             @RequestParam("sortField") String sortField,
                             @RequestParam("sortDir") String sortDir,
                             @RequestParam("keyword") String keyword){
        Page<User> userPage = userService.listByPage(pageNum, sortField, sortDir, keyword);

        long startCount = (pageNum-1)* Constant.USERS_IN_PAGE + 1;
        long endCountExpected = startCount + Constant.USERS_IN_PAGE -1;
        long endCount = (endCountExpected > userPage.getTotalElements()) ?
                userPage.getTotalElements() : endCountExpected;

        String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
        model.addAttribute("startCount", startCount);
        model.addAttribute("pageNum", pageNum);
        model.addAttribute("totalsPage", userPage.getTotalPages());
        model.addAttribute("endCount", endCount);
        model.addAttribute("listUsers", userPage.getContent());
        model.addAttribute("totalItems", userPage.getTotalElements());
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", reverseSortDir);
        model.addAttribute("keyword", keyword);
        model.addAttribute("titlePage", "Quản Lý Người Dùng");

        return "users/users";
    }

    @GetMapping("/users/new")
    public String newUser(Model model) {
        List<Role> listRoles = userService.listRoles();
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        model.addAttribute("pageTitle", "Thêm Người Dùng");
        return "users/users_form";
    }

    @PostMapping("/users/save")
    public String saveUser(User user, RedirectAttributes redirectAttributes,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replace(
                    " ", "");
            user.setPhotos(fileName);
            User userSaved = userService.save(user);

            String uploadDir = "user-photos/" + userSaved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.save(user);
        }
        redirectAttributes.addFlashAttribute("message", "Thêm người dùng thành công!");
        return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+user.getEmail();
    }

    @GetMapping("/users/edit/{id}")
    public String editUser(@PathVariable(name = "id") Integer id,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = userService.get(id);
            List<Role> listRoles = userService.listRoles();
            model.addAttribute("user", user);
            model.addAttribute("listRoles", listRoles);
            model.addAttribute("pageTitle", "Cập Nhật Người Dùng");
            return "users/users_form";
        } catch (UserNotFoundExeption e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) throws UserNotFoundExeption {
        try {
            userService.delete(id);
            redirectAttributes.addFlashAttribute("message", "Xóa thành công người dùng có ID "+ id);
        } catch (UserNotFoundExeption exeption) {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy người dùng có ID " +id);
        }
        return "redirect:/users";
    }

    @GetMapping("/users/{id}/enabled/{enabled}")
    public String updateEnabledUser(@PathVariable(name = "id") Integer id, @PathVariable(name =
            "enabled") boolean enabled, RedirectAttributes redirectAttributes){

            userService.updateEnabledStatus(id, enabled);
            String message = enabled ? "Kích hoạt" : "Vô hiệu hóa";
            redirectAttributes.addFlashAttribute("message", message + " thành công người dùng có " +
                    "ID "+ id);

        return "redirect:/users";
    }

    @GetMapping("/users/export/csv")
    public void exportToCsv(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserCsvExporter userCsvExporter = new UserCsvExporter();
        userCsvExporter.export(userList,response);
    }

    @GetMapping("/users/export/pdf")
    public void exportToPdf(HttpServletResponse response) throws IOException {
        List<User> userList = userService.listAll();
        UserPdfExporter userPdfExporter = new UserPdfExporter();
        userPdfExporter.export(userList,response);
    }

}
