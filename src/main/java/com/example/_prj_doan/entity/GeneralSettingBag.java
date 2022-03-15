package com.example._prj_doan.entity;

import com.example._prj_doan.manager.constain.Constant;

import java.util.List;

public class GeneralSettingBag extends SettingBag {
    public GeneralSettingBag(List<Setting> listSettings) {
        super(listSettings);
    }

    public void updateCurrencySymbol(String value){
        super.update("CURRENCY_SYMBOL", value);
    }
    public void updateSiteLogo(String value){
        super.update("SITE_LOGO", Constant.BASE_URL_AWS+ value);
    }

}
