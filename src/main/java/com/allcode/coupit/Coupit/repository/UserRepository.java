package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.User;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long>, CustomUserRepository {


}
