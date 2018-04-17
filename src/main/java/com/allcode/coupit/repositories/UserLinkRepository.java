package com.allcode.coupit.repositories;

import com.allcode.coupit.models.UserLink;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserLinkRepository extends CrudRepository<UserLink, Long> {
    UserLink findByUid(String uid);
}
