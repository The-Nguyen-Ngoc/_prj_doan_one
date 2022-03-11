package com.example._prj_doan.manager.controller;

import com.example._prj_doan.entity.Currency;
import com.example._prj_doan.entity.GeneralSettingBag;
import com.example._prj_doan.entity.Setting;
import com.example._prj_doan.manager.repository.CurrencyRepo;
import com.example._prj_doan.manager.service.SettingService;
import com.example._prj_doan.manager.utils.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class SettingController {

    @Autowired
    private SettingService settingService;
    @Autowired
    private CurrencyRepo currencyRepo;

    @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> settingList = settingService.listAllSetting();
        List<Currency> listCurrencies = currencyRepo.findAllByOrderByNameAsc();

        model.addAttribute("listCurrencies", listCurrencies);

        for (Setting setting : settingList) {
            model.addAttribute(setting.getKey(), setting.getValue());
        }
        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage") MultipartFile multipartFile,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag settingBag = settingService.getGeneralSettings();

        saveSiteLogo(multipartFile,settingBag);
        saveCurrencySymbol(request, settingBag);
        updateSettingValuesFromForm(request, settingBag.list());

        redirectAttributes.addFlashAttribute("message", "Cài đặt thành công!");

        return "redirect:/settings";
    }

    private void saveSiteLogo(MultipartFile multipartFile, GeneralSettingBag settingBag) throws IOException {
        if(!multipartFile.isEmpty()){
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String value = "site-logo/" + fileName;
            settingBag.updateSiteLogo(value);
            String uploadDir = "site-logo/";
            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
    }

    private void saveCurrencySymbol(HttpServletRequest request, GeneralSettingBag settingBag){
        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency> optionalCurrency = currencyRepo.findById(currencyId);

        if(optionalCurrency.isPresent()){
            Currency currency = optionalCurrency.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }
    }

    private void updateSettingValuesFromForm(HttpServletRequest request, List<Setting> listSetting){
        for(Setting setting : listSetting){
            String value = request.getParameter(setting.getKey());
            if(value!= null ){
                setting.setValue(value);
            }
        }

        settingService.saveAll(listSetting);
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){

        List<Setting> listSetting = settingService.getMailServerSettings();
        updateSettingValuesFromForm(request, listSetting);

        redirectAttributes.addFlashAttribute("message", "Cài đặt Mail Server thành công!");

        return "redirect:/settings";

    }
    @PostMapping("/settings/save_mail_templates")
    public String saveMailtemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){

        List<Setting> listSetting = settingService.getMailTemplateSettings();
        updateSettingValuesFromForm(request, listSetting);

        redirectAttributes.addFlashAttribute("message", "Cài đặt Mail Template thành công!");

        return "redirect:/settings";

    }
    @PostMapping("/settings/save_payment")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){

        List<Setting> listSetting = settingService.getPaymentSettings();
        updateSettingValuesFromForm(request, listSetting);

        redirectAttributes.addFlashAttribute("message", "Cài đặt phương thức thanh toán thành công!");

        return "redirect:/settings";

    }
}
