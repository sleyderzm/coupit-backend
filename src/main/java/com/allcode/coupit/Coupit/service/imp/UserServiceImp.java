package com.allcode.coupit.Coupit.service.imp;

import java.util.List;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.repository.UserDAO;
import com.allcode.coupit.Coupit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Transactional
    public void save(User user) {
        userDAO.save(user);
    }

    @Transactional
    public void delete(User user) {
        userDAO.delete(user);
    }

    @Transactional(readOnly = true)
    public List list() {
        return userDAO.list();
    }

    @Override
    public User findById(int id) {
        return userDAO.findById(id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        return userDAO.findByEmailAndPassword(email,password);
    }

    @Override
    public User findByEmail(String email) {
        return userDAO.findByEmail(email);
    }

}