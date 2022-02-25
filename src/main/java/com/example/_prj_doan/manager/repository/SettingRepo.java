package com.example._prj_doan.manager.repository;

import com.example._prj_doan.entity.Setting;
import com.example._prj_doan.entity.SettingCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SettingRepo extends CrudRepository<Setting, String> {
     List<Setting> findByCategory(SettingCategory category);
}
