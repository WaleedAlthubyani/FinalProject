package com.example.dealify.Repository;

import com.example.dealify.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Ebtehal
@Repository
public interface AuthRepository extends JpaRepository<MyUser,Integer> {
    MyUser findMyUserByUsername(String userName);
    MyUser findMyUserById(Integer id);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}