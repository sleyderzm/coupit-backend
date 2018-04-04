package com.allcode.coupit.Coupit.repository.impl;

import com.allcode.coupit.Coupit.model.User;

import com.allcode.coupit.Coupit.repository.CustomUserRepository;

public class CustomUserRepositoryImpl implements CustomUserRepository {


    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public User findByEmailAndPassword(String email, String digestPassword) {
        return null;
    }
}
