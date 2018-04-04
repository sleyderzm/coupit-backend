package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.model.User;

public interface CustomSessionRepository {

    User getCurrentUser();

    Session findByToken(String token);
}
