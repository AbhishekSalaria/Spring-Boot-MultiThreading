package com.multithreading.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.multithreading.Entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
