package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.User;

public interface CustomUserRepository {

    User findByEmail(String email);

    User findByEmailAndPassword(String email, String digestPassword);

}
