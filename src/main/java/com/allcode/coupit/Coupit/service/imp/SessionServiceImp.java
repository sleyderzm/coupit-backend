package com.allcode.coupit.Coupit.service.imp;

import com.allcode.coupit.Coupit.model.Session;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.repository.SessionDAO;
import com.allcode.coupit.Coupit.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class SessionServiceImp implements SessionService {

    @Autowired
    private SessionDAO sessionDAO;

    @Transactional
    public void save(Session session) {
        sessionDAO.save(session);
    }

    public Session findByToken(String token) {
        return sessionDAO.findByToken(token);
    }

    @Override
    public List list() {
        return sessionDAO.list();
    }

    @Override
    public User getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();
        String token = request.getParameter("token");
        if(token != null) {
            Session session = this.findByToken(token);
            return session.getUser();
        }
        return null;
    }

}