package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.Session;
import java.util.List;

public interface SessionDAO {
    void save(Session session);
    List list();
    Session findByToken(String token);
}
