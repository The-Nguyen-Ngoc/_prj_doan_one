package com.example._prj_doan.manager.service;

import com.example._prj_doan.entity.Country;
import com.example._prj_doan.entity.ShippingRate;
import com.example._prj_doan.entity.User;
import com.example._prj_doan.manager.constain.Constant;
import com.example._prj_doan.manager.exception.ShippingRateAlreadyException;
import com.example._prj_doan.manager.repository.CountryRepository;
import com.example._prj_doan.manager.repository.ShippingRateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ShippingRateService {
    public static final int RATES_PER_PAGE = 10;

    @Autowired private ShippingRateRepo shippingRateRepository;
    @Autowired private CountryRepository countryRepository;

    public Page<ShippingRate> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest page = PageRequest.of(pageNum-1,RATES_PER_PAGE, sort);

        if(keyword != null){
            return shippingRateRepository.findAll(keyword, page);
        }
        return shippingRateRepository.findAll(page);
    }

    public List<Country> listAllCountries(){
        return countryRepository.findAllByOrderByNameAsc();
    }

    public void save(ShippingRate shippingRate) throws ShippingRateAlreadyException {
        ShippingRate rate  = shippingRateRepository.findByCountryAndState(shippingRate.getCountry().getId(), shippingRate.getState());

        boolean foundExistingRateInNewMode = shippingRate.getId() == null && rate != null;
        boolean foundExistingRateInEditMode = shippingRate.getId() != null && rate != null;

        if(foundExistingRateInNewMode || foundExistingRateInEditMode){
            throw new ShippingRateAlreadyException("Shipping rate already exists");
        }

        shippingRateRepository.save(shippingRate);
    }

    public ShippingRate get(Integer id) throws ShippingRateAlreadyException {
        try {
            return shippingRateRepository.findById(id).get();
        }catch (NoSuchElementException e) {
            throw new ShippingRateAlreadyException("Shipping rate not found");
        }
    }

    public void updateCODSupport(Integer id, boolean codSupport) throws ShippingRateAlreadyException {
        Long count  = shippingRateRepository.countById(id);

        if(count == null|| count == 0){
            throw new ShippingRateAlreadyException("Shipping rate not found witd id: " + id);
        }

        shippingRateRepository.updateCodSupported(id, codSupport);
    }

    public void delete(Integer id) throws ShippingRateAlreadyException {
        Long count = shippingRateRepository.countById(id);
        if(count == null || count == 0){
            throw new ShippingRateAlreadyException("Shipping rate not found witd id: " + id);
        }

        shippingRateRepository.deleteById(id);
    }
}
