package com.allcode.coupit.Coupit.repository.imp;

import java.util.List;
import com.allcode.coupit.Coupit.model.User;
import com.allcode.coupit.Coupit.repository.UserDAO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class UserDAOImp implements UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(User user) {
        if(user.getId() != null){
            sessionFactory.getCurrentSession().update(user);
        }else{
            sessionFactory.getCurrentSession().save(user);
        }

    }

    @Override
    public void delete(User user) {
        sessionFactory.getCurrentSession().delete(user);
    }

    @Override
    public List list() {
        @SuppressWarnings("unchecked")
        Query query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.list();
    }

    @Override
    public User findById(int id) {
        Session session = sessionFactory.getCurrentSession();
        return (User)session.get(User.class,id);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        return (User)sessionFactory.getCurrentSession().createQuery("from User WHERE email = :email AND password = :password")
                .setMaxResults(1).setParameter("email", email).setParameter("password", password).uniqueResult();
    }


    @Override
    public User findByEmail(String email) {
        return (User)sessionFactory.getCurrentSession().createQuery("from User WHERE email = :email")
                .setMaxResults(1).setParameter("email", email).uniqueResult();
    }
}