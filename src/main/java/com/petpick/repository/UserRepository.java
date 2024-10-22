package com.petpick.repository;


import com.petpick.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    public User save(User user);
    public User findByUserId(Long id);

}