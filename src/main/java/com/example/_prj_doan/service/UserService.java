package com.example._prj_doan.service;

import com.example._prj_doan.constain.Constant;
import com.example._prj_doan.entity.Role;
import com.example._prj_doan.entity.User;
import com.example._prj_doan.exception.UserNotFoundExeption;
import com.example._prj_doan.repository.RoleRepo;
import com.example._prj_doan.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * @author TheNN
 */
@Service
@Transactional
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;
    @Autowired
    BCryptPasswordEncoder passwordEncoder;


//    public List<User> listAll(){
//        return userRepo.findAll();
//    }

    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        PageRequest page = PageRequest.of(pageNum-1, Constant.USERS_IN_PAGE, sort);

        if(keyword != null){
            return userRepo.findAll(keyword, page);
        }
        return userRepo.findAll(page);
    }

    public User save(User user) {
        boolean isUpdatingUser = (user.getId()!=null);

        if(isUpdatingUser){
            User existingUser = userRepo.findById(user.getId()).get();
            if(user.getPassword().isEmpty()){
                user.setPassword(existingUser.getPassword());
            }else {
                encodePassword(user);
            }
        }
        else {
            encodePassword(user);
        }
        return userRepo.save(user);
    }

    private void encodePassword(User user){
        String encodedPass = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPass);
    }

    public boolean isEmailUnique(Integer id ,String email){
        User userByEmail = userRepo.getUserByEmail(email);

        if(userByEmail == null) return true;

        boolean isCreatingNew = (id == null);

        if(isCreatingNew){
            if(userByEmail !=null) return false;
        }
        else {
            if(userByEmail.getId() != id){
                return false;
            }
        }

        return true;
    }

    public User get(Integer id) throws UserNotFoundExeption {
        try {
            return userRepo.findById(id).get();
        }catch (NoSuchElementException e){
            throw new UserNotFoundExeption("Không tìm thấy người dùng có ID "+ id);
        }
    }

    public void delete(Integer id) throws UserNotFoundExeption {
        Long countById = userRepo.countById(id);
        if(countById ==null || countById ==0){
            throw new UserNotFoundExeption("Không tìm thấy người dùng có ID: " + id);
        }
        else {
            userRepo.deleteById(id);
        }
    }

    public void updateEnabledStatus(Integer id, boolean enabled){
        userRepo.updateEnabledStatus(id,enabled);
    }

    public List<User> listAll() {
        return (List<User>) userRepo.findAll(Sort.by("firstName"));
    }

    public List<Role> listRoles() {
        return roleRepo.findAll();
    }
}
