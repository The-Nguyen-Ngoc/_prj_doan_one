package com.example._prj_doan.controller;


import com.example._prj_doan.config.LoginUserDetails;
import com.example._prj_doan.entity.User;
import com.example._prj_doan.service.UserService;
import com.example._prj_doan.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
public class AccountController {
    @Autowired
    UserService userService;

    @GetMapping("/account")
    public String viewDetailsUser(@AuthenticationPrincipal LoginUserDetails loginUserDetails,
                                  Model model){
        User user = userService.getUserByEmail(loginUserDetails.getUsername());

        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Edit Account");
        return "users/account_form";
    }

    @PostMapping("/account/update")
    public String updateAccount(User user, RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal LoginUserDetails loginUserDetails,
                           @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename()).replace(
                    " ", "");
            user.setPhotos(fileName);
            User userSaved = userService.updateAccount(user);

            String uploadDir = "user-photos/" + userSaved.getId();
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        }
        else {
            if(user.getPhotos().isEmpty()) user.setPhotos(null);
            userService.updateAccount(user);
        }
        loginUserDetails.setFirstName(user.getFirstName());
        loginUserDetails.setLastName(user.getLastName());

        redirectAttributes.addFlashAttribute("message", "Thay đổi thông tin Account thành công!");
        return "redirect:/account";
    }
}
