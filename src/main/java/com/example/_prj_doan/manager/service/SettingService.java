package com.example._prj_doan.manager.service;

import com.example._prj_doan.entity.GeneralSettingBag;
import com.example._prj_doan.entity.Setting;
import com.example._prj_doan.entity.SettingCategory;
import com.example._prj_doan.manager.repository.SettingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired
    private SettingRepo settingRepository;

    public List<Setting> listAllSetting(){
        return (List<Setting>) settingRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        List<Setting> generalSettings = new ArrayList<>();
        List<Setting> settings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> settingsCurrent = settingRepository.findByCategory(SettingCategory.CURRENCY);
        generalSettings.addAll(settings);
        generalSettings.addAll(settingsCurrent);

        return new GeneralSettingBag(generalSettings) ;

    }

    public void saveAll(Iterable<Setting> settings){
        settingRepository.saveAll(settings);
    }

    public List<Setting> getMailServerSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getMailTemplateSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }
}
