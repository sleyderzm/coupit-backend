package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.Role;

import java.util.List;

public interface RoleDAO {
    void save(Role role);
    void delete(Role role);
    List list();
    Role findById(int id);
}
