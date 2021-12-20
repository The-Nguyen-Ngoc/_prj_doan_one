package com.example._prj_doan.repository;

import com.example._prj_doan.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends PagingAndSortingRepository<User, Integer> {
    @Query("select u FROM User u WHERE u.email = :email")
    User getUserByEmail(@Param("email") String email);

    Long countById(Integer id);

    @Query("update User u set u.enabled = ?2 where u.id =?1")
    @Modifying
    void updateEnabledStatus(Integer id, boolean enabled);

    @Query("SELECT  u from User u WHERE CONCAT(u.id,' ', u.email, ' ', u.firstName, ' ',u.lastName) " +
            "LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);
}
