package com.allcode.coupit.repositories;

import org.springframework.data.repository.CrudRepository;
import com.allcode.coupit.models.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}
