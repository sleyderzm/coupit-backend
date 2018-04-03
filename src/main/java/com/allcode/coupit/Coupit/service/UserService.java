package com.allcode.coupit.Coupit.service;

import com.allcode.coupit.Coupit.model.User;
import java.util.List;

public interface UserService {
    void save(User user);
    void delete(User user);
    List list();
    User findById(int id);
    User findByEmailAndPassword(String email, String password);
    User findByEmail(String email);
}
