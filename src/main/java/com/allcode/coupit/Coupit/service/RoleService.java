package com.allcode.coupit.Coupit.service;

import com.allcode.coupit.Coupit.model.Role;
import java.util.List;

public interface RoleService {
    void save(Role role);
    void delete(Role role);
    List list();
    Role findById(Integer id);
}
