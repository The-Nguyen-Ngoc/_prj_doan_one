package com.example._prj_doan.manager.config;

import com.example._prj_doan.entity.User;
import com.example._prj_doan.manager.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class LoginUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepo.getUserByEmail(email);
        if(user != null){
            return new LoginUserDetails(user);
        }
        throw new UsernameNotFoundException("Không tìm thấy người dùng có Email: "+ email);
    }
}
