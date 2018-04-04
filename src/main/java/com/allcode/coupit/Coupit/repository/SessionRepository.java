package com.allcode.coupit.Coupit.repository;

import com.allcode.coupit.Coupit.model.Session;

import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Session, Long>, CustomSessionRepository {

}
