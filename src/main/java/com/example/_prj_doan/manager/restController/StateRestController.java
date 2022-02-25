package com.example._prj_doan.manager.restController;

import com.example._prj_doan.entity.Country;
import com.example._prj_doan.entity.State;
import com.example._prj_doan.manager.dto.StateDto;
import com.example._prj_doan.manager.repository.StateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StateRestController {
    @Autowired private StateRepository stateRepository;

    @GetMapping("/states/list_by_country/{id}")
    public List<StateDto> listByCountry(@PathVariable("id") Integer countryId) {
        List<State> states = stateRepository.findAllByCountryOrderByNameAsc(new Country(countryId));
        List<StateDto> stateDtos = new ArrayList<>();

        for(State state : states) {
            stateDtos.add(new StateDto(state.getId(), state.getName()));
        }

        return stateDtos;

    }

    @PostMapping("/states/save")
    public String save(@RequestBody State state){
        State savedState = stateRepository.save(state);
        return "Saved state: " + savedState.getId();
    }

    @DeleteMapping("/states/delete/{id}")
    public void delete (@PathVariable("id") Integer id) {
        stateRepository.deleteById(id);
    }
}
