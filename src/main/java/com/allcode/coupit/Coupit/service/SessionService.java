package com.allcode.coupit.Coupit.service;

import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.model.User;
import java.util.List;

public interface SessionService {
    void save(Session session);
    Session findByToken(String token);
    List list();
    User getCurrentUser();
}
