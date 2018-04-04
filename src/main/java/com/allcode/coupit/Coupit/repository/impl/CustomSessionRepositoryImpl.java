package com.allcode.coupit.Coupit.repository.impl;

import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.repository.CustomSessionRepository;
import com.allcode.coupit.Coupit.repository.CustomUserRepository;

public class CustomSessionRepositoryImpl implements CustomSessionRepository {

    @Override
    public User getCurrentUser() {
        return null;
    }

    @Override
    public Session findByToken(String token) {
        return null;
    }
}
