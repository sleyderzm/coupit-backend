package com.allcode.coupit.Coupit.repository.imp;

import com.allcode.coupit.Coupit.model.Role;
import com.allcode.coupit.Coupit.repository.RoleDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class RoleDAOImp implements RoleDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(Role role) {
        sessionFactory.getCurrentSession().save(role);
    }

    @Override
    public void delete(Role role) {
        sessionFactory.getCurrentSession().delete(role);
    }

    @Override
    public List list() {
        @SuppressWarnings("unchecked")
        Query query = sessionFactory.getCurrentSession().createQuery("from Role");
        return query.list();
    }

    @Override
    public Role findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return (Role)session.get(Role.class,id);
    }
}