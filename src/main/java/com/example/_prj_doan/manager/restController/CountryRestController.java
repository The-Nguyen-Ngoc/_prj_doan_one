package com.example._prj_doan.manager.restController;

import com.example._prj_doan.entity.Country;
import com.example._prj_doan.manager.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CountryRestController {

    @Autowired private CountryRepository countryRepository;

    @GetMapping("/countries/list")
   public List<Country> listAll() {
        return countryRepository.findAllByOrderByNameAsc();
    }
    @PostMapping("/countries/save")
    public String save(@RequestBody Country country){
        Country savedCountry = countryRepository.save(country);
        return String.valueOf(savedCountry.getId());
    }

    @GetMapping("/countries/delete/{code}")
    public void delete(@PathVariable("code") String code){
        countryRepository.deleteByCode(code);
    }
}
